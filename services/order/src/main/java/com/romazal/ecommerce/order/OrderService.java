package com.romazal.ecommerce.order;

import com.romazal.ecommerce.customer.CustomerClient;
import com.romazal.ecommerce.exception.BusinessException;
import com.romazal.ecommerce.exception.MicroserviceBusinessException;
import com.romazal.ecommerce.kafka.notification.NotificationKafkaTemplate;
import com.romazal.ecommerce.kafka.notification.OrderCancellationNotification;
import com.romazal.ecommerce.kafka.notification.OrderConfirmationNotification;
import com.romazal.ecommerce.kafka.notification.OrderPaymentLinkNotification;
import com.romazal.ecommerce.order_item.OrderItemRequest;
import com.romazal.ecommerce.order_item.OrderItemService;
import com.romazal.ecommerce.payment.PaymentClient;
import com.romazal.ecommerce.product.ProductClient;
import com.romazal.ecommerce.product.PurchaseRequest;
import com.romazal.ecommerce.shipment.ShipmentClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.romazal.ecommerce.order.OrderStatus.*;
import static com.romazal.ecommerce.order_item.OrderItemsStatus.RESERVED;
import static com.romazal.ecommerce.order_item.OrderItemsStatus.UNRESERVED;
import static com.romazal.ecommerce.payment.PaymentStatus.*;
import static com.romazal.ecommerce.payment.PaymentStatus.PENDING;
import static java.lang.String.format;
import static org.apache.commons.lang.StringUtils.isNotBlank;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {

    private final OrderRepository repository;
    private final OrderMapper mapper;
    private final OrderItemService orderItemService;
    private final ProductClient productClient;
    private final CustomerClient customerClient;
    private final PaymentClient paymentClient;
    private final ShipmentClient shipmentClient;
    private final NotificationKafkaTemplate notificationKafkaTemplate;

    public UUID createOrder(OrderRequest orderRequest) {
        log.info("Starting to create order for customer ID: {}", orderRequest.customerId());

        // Map the order request to the Order entity
        Order order = mapper.toOrder(orderRequest);
        log.info("Mapped OrderRequest to Order entity: {}", order);

        // Fetch customer details
        var customer = customerClient.findCustomerById(orderRequest.customerId())
                .orElseThrow(() -> {
                    log.error("Customer with ID {} not found", orderRequest.customerId());
                    return new MicroserviceBusinessException(
                            String.format("No customer with such ID exists:: %s", orderRequest.customerId())
                    );
                });
        log.info("Customer found: {}", customer);

        // Validate order status
        if (order.getOrderStatus() != UNFINISHED && order.getOrderStatus() != OrderStatus.PENDING) {
            log.error("Invalid order status: {}. Must be UNFINISHED or PENDING.", orderRequest.orderStatus());
            throw new IllegalArgumentException(
                    String.format("Invalid order status: %s. Must be either UNFINISHED or PENDING.", orderRequest.orderStatus())
            );
        }

        // Check if order already exists
        if (orderRequest.orderId() != null && repository.existsById(orderRequest.orderId())) {
            log.error("Order with ID {} already exists", orderRequest.orderId());
            throw new IllegalArgumentException(
                    String.format("Order already exists with the provided ID:: %s", orderRequest.orderId())
            );
        }
        log.info("Order ID is unique and valid.");

        order.setCustomerEmail(customer.email());
        order.setCustomerName(customer.firstname() + " " + customer.lastname());

        // Save order to repository
        UUID responseOrderId = repository.save(order).getOrderId();
        log.info("Order saved with ID: {}", responseOrderId);

        // Save each order item
        for (PurchaseRequest purchaseRequest : orderRequest.orderItems()) {
            log.info("Processing order item with Product ID: {}", purchaseRequest.productId());
            orderItemService.saveOrderItem(
                    new OrderItemRequest(
                            null,
                            responseOrderId,
                            purchaseRequest.productId(),
                            purchaseRequest.quantity(),
                            purchaseRequest.unitPrice()
                    )
            );
            log.info("Order item saved for Product ID: {}", purchaseRequest.productId());
        }

        // Queue the order if status is PENDING
        if (order.getOrderStatus() == OrderStatus.PENDING) {
            log.info("Order status is PENDING; queuing order with ID: {}", order.getOrderId());
            queueUpOrder(order.getOrderId());
        }

        log.info("Order creation completed successfully with Order ID: {}", responseOrderId);
        return responseOrderId;
    }


    public OrderResponse getOrderById(UUID orderId) {
        return repository.findById(orderId).map(mapper::toOrderResponse)
                .orElseThrow(() -> new IllegalArgumentException(
                        format("No order found with the provided ID:: %s", orderId)
                ));
    }

    public List<OrderResponse> getAllOrders() {
        return repository.findAll()
                .stream()
                .map(mapper::toOrderResponse)
                .collect(Collectors.toList());
    }

    public void updateOrder(OrderUpdateRequest orderUpdateRequest) {
        var order = repository.findById(orderUpdateRequest.orderId())
                .orElseThrow(() -> new IllegalArgumentException(
                        format("No order found with the provided ID:: %s", orderUpdateRequest.orderId())
                ));

        OrderStatus initialStatus = order.getOrderStatus();

        if (initialStatus != UNFINISHED) {
            throw new BusinessException(
                    format("Order is already completed, current status:: %s,", initialStatus)
            );
        }

        if (orderUpdateRequest.orderStatus() != UNFINISHED && orderUpdateRequest.orderStatus() != OrderStatus.CONFIRMED) {
            throw new java.lang.IllegalArgumentException(
                    format("Invalid order status: %s. Must be either UNFINISHED or PENDING.", orderUpdateRequest.orderStatus())
            );
        }

        mergeOrder(order, orderUpdateRequest);

        if (order.getOrderStatus() == OrderStatus.PENDING) {
            queueUpOrder(order.getOrderId());
        }

        repository.save(order);

    }

    private void mergeOrder(Order order, OrderUpdateRequest orderUpdateRequest) {

        if (orderUpdateRequest.orderId() != null) {
            order.setOrderId(orderUpdateRequest.orderId());
        }

        if (orderUpdateRequest.customerId() != null) {
            order.setCustomerId(orderUpdateRequest.customerId());
        }

        if (orderUpdateRequest.totalAmount() != null) {
            order.setTotalAmount(orderUpdateRequest.totalAmount());
        }

        if (orderUpdateRequest.paymentStatus() != null) {
            order.setPaymentStatus(orderUpdateRequest.paymentStatus());
        }

        if (orderUpdateRequest.orderStatus() != null) {
            order.setOrderStatus(orderUpdateRequest.orderStatus());
        }

        if (orderUpdateRequest.paymentMethod() != null) {
            order.setPaymentMethod(orderUpdateRequest.paymentMethod());
        }

        if (isNotBlank(orderUpdateRequest.shippingAddress())) {
            order.setShippingAddress(orderUpdateRequest.shippingAddress());
        }

        if (orderUpdateRequest.orderItems() != null) {

            orderItemService.deleteAllOrderItemsByOrderId(order.getOrderId());

            for (PurchaseRequest purchaseRequest : orderUpdateRequest.orderItems()) {
                orderItemService.saveOrderItem(
                        new OrderItemRequest(
                                null,
                                order.getOrderId(),
                                purchaseRequest.productId(),
                                purchaseRequest.quantity(),
                                purchaseRequest.unitPrice()
                        )
                );
            }
        }

    }

    public void deleteOrder(UUID orderId) {
        var order = repository.findById(orderId).orElseThrow(() -> new IllegalArgumentException(
                format("No order found with the provided ID:: %s", orderId)
        ));

        if (order.getOrderStatus() != UNFINISHED) {
            throw new BusinessException(
                    format("Order is already completed, current status:: %s,", order.getOrderStatus())
            );
        }

        orderItemService.deleteAllOrderItemsByOrderId(orderId);

        repository.delete(order);
    }

    public void cancelOrder(UUID orderId) {
        var order = repository.findById(orderId).orElseThrow(() -> new IllegalArgumentException(
                format("No order found with the provided ID:: %s", orderId)
        ));

        if (order.getOrderStatus() == UNFINISHED) {
            deleteOrder(orderId);
            return;
        } else if (order.getOrderStatus() == CANCELED) {
            return;
        }

        if (order.getOrderStatus() == DELIVERED &&
                order.getLastModifiedDate().plusWeeks(2).isBefore(LocalDateTime.now())
        ) {
            throw new BusinessException(
                    "Cannot cancel the order, it's been more then two weeks after receiving the products of the order"
            );
        }

        if (order.getPaymentStatus() == PENDING) {
            paymentClient.failPaymentByOrderId(order.getOrderId());
        } else if (order.getPaymentStatus() == PAID) {
            paymentClient.refundPaymentByOrderId(order.getOrderId());
        }

        if (order.getOrderStatus() == CONFIRMED || order.getOrderStatus() == SHIPPED || order.getOrderStatus() == DELIVERED) {
            shipmentClient.refundShipmentByOrderId(order.getOrderId());
        }

        var orderItems = orderItemService.getAllOrderItemsByOrderIdToPurchaseRequest(orderId);

        if (order.getOrderItemsStatus() == RESERVED) {
            productClient.refundProducts(orderItems);

            order.setOrderItemsStatus(UNRESERVED);
        }

        order.setOrderStatus(CANCELED);
        order.setPaymentStatus(REFUNDED);

        notificationKafkaTemplate.sendOrderCancellationNotification(
                new OrderCancellationNotification(
                        order.getOrderId(),
                        order.getCustomerEmail(),
                        order.getCustomerName(),
                        order.getTotalAmount(),
                        orderItems,
                        order.getCreatedDate()
                )
        );

        repository.save(order);
    }

    public UUID queueUpOrder(UUID orderId) {
        var order = repository.findById(orderId).orElseThrow(() -> new IllegalArgumentException(
                format("No order found with the provided ID:: %s", orderId)
        ));

        if (
                order.getOrderStatus() == CONFIRMED
                        || order.getOrderStatus() == SHIPPED
                        || order.getOrderStatus() == DELIVERED
                        || order.getOrderStatus() == CANCELED
        ) {
            throw new BusinessException(
                    format("Order is already confirmed, current status:: %s,", order.getOrderStatus())
            );
        }

        if (order.getOrderItemsStatus() == UNRESERVED) {
            productClient.purchaseProducts(
                    orderItemService.getAllOrderItemsByOrderIdToPurchaseRequest(orderId)
            );

            order.setOrderItemsStatus(RESERVED);
        }

        order.setOrderStatus(OrderStatus.PENDING);
        order.setPaymentStatus(PENDING);

        var paymentResponse = mapper.toPaymentResponse(order);

        UUID paymentId = paymentClient.createPayment(paymentResponse);

        notificationKafkaTemplate.sendOrderPaymentLinkNotification(
                new OrderPaymentLinkNotification(
                        order.getOrderId(),
                        paymentId,
                        order.getCustomerEmail(),
                        order.getCustomerName(),
                        order.getTotalAmount(),
                        orderItemService.getAllOrderItemsByOrderIdToPurchaseRequest(order.getOrderId())
                )
        );

        repository.save(order);

        return paymentId;
    }

    public UUID confirmOrder(UUID orderId) {
        var order = repository.findById(orderId).orElseThrow(() -> new IllegalArgumentException(
                format("No order found with the provided ID:: %s", orderId)
        ));

        if (
                order.getOrderStatus() == CONFIRMED
                        || order.getOrderStatus() == SHIPPED
                        || order.getOrderStatus() == DELIVERED
                        || order.getOrderStatus() == CANCELED
        ) {
            throw new BusinessException(
                    format("Order is already confirmed, current status:: %s,", order.getOrderStatus())
            );
        }

        if (order.getOrderItemsStatus() == UNRESERVED) {
            productClient.purchaseProducts(
                    orderItemService.getAllOrderItemsByOrderIdToPurchaseRequest(orderId)
            );

            order.setOrderItemsStatus(RESERVED);
        }

        order.setOrderStatus(CONFIRMED);
        order.setPaymentStatus(PAID);

        var shippingResponse = mapper.toShippingResponse(order);

        shipmentClient.createShipping(shippingResponse);

        notificationKafkaTemplate.sendOrderConfirmationNotification(
                new OrderConfirmationNotification(
                        order.getOrderId(),
                        order.getCustomerEmail(),
                        order.getCustomerName(),
                        order.getTotalAmount(),
                        orderItemService.getAllOrderItemsByOrderIdToPurchaseRequest(order.getOrderId())
                )
        );

        return repository.save(order).getOrderId();
    }

    public List<OrderResponse> getAllOrdersByCustomerId(Long customerId) {
        return repository.findAllByCustomerId(customerId)
                .stream()
                .map(mapper::toOrderResponse)
                .toList();
    }

    public UUID updateOrderStatus(UUID orderId, OrderStatus orderStatus) {
        var order = repository.findById(orderId).orElseThrow(() -> new IllegalArgumentException(
                format("No order found with the provided ID:: %s", orderId)
        ));

        order.setOrderStatus(orderStatus);

        return repository.save(order).getOrderId();
    }

    public UUID setOrderStatusToShipped(UUID orderId) {
        return updateOrderStatus(orderId, SHIPPED);
    }

    public UUID setOrderStatusToDelivered(UUID orderId) {
        return updateOrderStatus(orderId, DELIVERED);
    }
}

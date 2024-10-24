package com.romazal.ecommerce.order;

import com.romazal.ecommerce.customer.CustomerClient;
import com.romazal.ecommerce.exception.BusinessException;
import com.romazal.ecommerce.exception.MicroserviceBusinessException;
import com.romazal.ecommerce.orderItem.OrderItemMapper;
import com.romazal.ecommerce.orderItem.OrderItemRequest;
import com.romazal.ecommerce.orderItem.OrderItemService;
import com.romazal.ecommerce.product.ProductClient;
import com.romazal.ecommerce.product.PurchaseRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.romazal.ecommerce.order.OrderStatus.*;
import static java.lang.String.format;
import static org.apache.commons.lang.StringUtils.isNotBlank;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository repository;
    private final OrderMapper mapper;
    private final OrderItemMapper orderItemMapper;
    private final OrderItemService orderItemService;
    private final ProductClient productClient;
    private final CustomerClient customerClient;
    //private final PaymentClient paymentClient;
    //private final OrderKafkaProducer orderKafkaProducer;


    public UUID createOrder(OrderRequest orderRequest) {
        Order order = mapper.toOrder(orderRequest);

        var customer = customerClient.findCustomerById(orderRequest.customerId())
                .orElseThrow(() -> new MicroserviceBusinessException(
                        format("No customer with such ID exists:: %s", orderRequest.customerId())
                ));

        if (orderRequest.orderStatus() != OrderStatus.UNFINISHED && orderRequest.orderStatus() != OrderStatus.CONFIRMED) {
            throw new IllegalArgumentException(
                    format("Invalid order status: %s. Must be either UNFINISHED or CONFIRMED.", orderRequest.orderStatus())
            );
        }

        if (repository.existsById(orderRequest.orderId())) {
            throw new IllegalArgumentException(
                    format("Order already exists with the provided ID:: %s", orderRequest.orderId())
            );
        }

        UUID responseOrderId = repository.save(order).getOrderId();

        for (PurchaseRequest purchaseRequest : orderRequest.orderItems()) {
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

        if (order.getOrderStatus() == OrderStatus.CONFIRMED) {
            order.setOrderStatus(UNFINISHED);
            repository.save(order);
            queueUpOrder(order.getOrderId());
        }

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

        if (orderUpdateRequest.orderStatus() != OrderStatus.UNFINISHED && orderUpdateRequest.orderStatus() != OrderStatus.CONFIRMED) {
            throw new java.lang.IllegalArgumentException(
                    format("Invalid order status: %s. Must be either UNFINISHED or CONFIRMED.", orderUpdateRequest.orderStatus())
            );
        }

        mergeOrder(order, orderUpdateRequest);

        if (order.getOrderStatus() == OrderStatus.CONFIRMED) {
            order.setOrderStatus(UNFINISHED);
            repository.save(order);
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
        }

        order.setOrderStatus(CANCELED);

        //todo
    }

    public UUID queueUpOrder(UUID orderId) {
        var order = repository.findById(orderId).orElseThrow(() -> new IllegalArgumentException(
                format("No order found with the provided ID:: %s", orderId)
        ));

        if (order.getOrderStatus() != UNFINISHED) {
            throw new BusinessException(
                    format("Order is already queued up, current status:: %s,", order.getOrderStatus())
            );
        }

        var purchasedProducts = productClient.purchaseProducts(
                orderItemService.getAllOrderItemsByOrderId(orderId)
                        .stream()
                        .map(orderItemMapper::toPurchaseRequest)
                        .toList()
        );

        order.setOrderStatus(PENDING);

        var paymentResponse = mapper.toPaymentResponse(order);

        //todo
        return null;
    }
}

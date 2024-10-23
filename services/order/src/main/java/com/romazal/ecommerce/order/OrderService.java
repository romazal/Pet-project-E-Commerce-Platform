package com.romazal.ecommerce.order;

import com.romazal.ecommerce.exception.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.romazal.ecommerce.order.OrderStatus.CANCELED;
import static com.romazal.ecommerce.order.OrderStatus.UNFINISHED;
import static java.lang.String.format;
import static org.apache.commons.lang.StringUtils.isNotBlank;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository repository;
    private final OrderMapper mapper;


    public UUID createOrder(OrderRequest orderRequest) {
        Order order = mapper.toOrder(orderRequest);
        return repository.save(order).getOrderId();
    }

    public OrderResponse getOrderById(UUID orderId) {
        return repository.findById(orderId).map(mapper::toOrderResponse)
                .orElseThrow(() -> new OrderNotFoundException(
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
                .orElseThrow(() -> new OrderNotFoundException(
                        format("No order found with the provided ID:: %s", orderUpdateRequest.orderId())
                ));

        if(order.getOrderStatus() != UNFINISHED) {
            throw new OrderIsFinishedException(
                    format("Order is already completed, current status:: %s,", order.getOrderStatus())
            );
        }

        mergeOrder(order, orderUpdateRequest);
        repository.save(order);
    }

    private void mergeOrder(Order order, OrderUpdateRequest orderUpdateRequest) {

        if(orderUpdateRequest.orderId() != null){
            order.setOrderId(orderUpdateRequest.orderId());
        }

        if(orderUpdateRequest.customerId() != null){
            order.setCustomerId(orderUpdateRequest.customerId());
        }

        if(orderUpdateRequest.totalAmount() != null){
            order.setTotalAmount(orderUpdateRequest.totalAmount());
        }

        if(orderUpdateRequest.paymentStatus() != null){
            order.setPaymentStatus(orderUpdateRequest.paymentStatus());
        }

        if(orderUpdateRequest.orderStatus() != null){
            order.setOrderStatus(orderUpdateRequest.orderStatus());
        }

        if(orderUpdateRequest.paymentMethod() != null){
            order.setPaymentMethod(orderUpdateRequest.paymentMethod());
        }

        if(isNotBlank(orderUpdateRequest.shippingAddress())){
            order.setShippingAddress(orderUpdateRequest.shippingAddress());
        }

        if(orderUpdateRequest.orderItems() != null){
            order.setOrderItems(orderUpdateRequest.orderItems());
        }

    }

    public void deleteOrder(UUID orderId) {
        var order = repository.findById(orderId).orElseThrow(() -> new OrderNotFoundException(
                format("No order found with the provided ID:: %s", orderId)
        ));

        if(order.getOrderStatus() != UNFINISHED) {
            throw new OrderIsFinishedException(
                    format("Order is already completed, current status:: %s,", order.getOrderStatus())
            );
        }

        repository.delete(order);
    }

    public void cancelOrder(UUID orderId) {
        var order = repository.findById(orderId).orElseThrow(() -> new OrderNotFoundException(
                format("No order found with the provided ID:: %s", orderId)
        ));

        if(order.getOrderStatus() == UNFINISHED) {
            deleteOrder(orderId);
            return;
        }

        order.setOrderStatus(CANCELED);

        //todo
    }
}

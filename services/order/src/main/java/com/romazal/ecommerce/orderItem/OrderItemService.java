package com.romazal.ecommerce.orderItem;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderItemService {

    private final OrderItemRepository repository;
    private final OrderItemMapper mapper;

    public UUID saveOrderItem(OrderItemRequest orderItemRequest) {
        var orderItem = mapper.toOrderItem(orderItemRequest);
        return repository.save(orderItem).getOrderItemId();
    }

    public List<OrderItemResponse> getAllOrderItemsByOrderId(UUID orderId) {
        return repository.findAllByOrder_OrderId(orderId)
                .stream()
                .map(mapper::toOrderItemResponse)
                .collect(Collectors.toList());
    }

    public void deleteAllOrderItemsByOrderId(UUID orderId) {
        repository.deleteAllByOrder_OrderId(orderId);
    }
}

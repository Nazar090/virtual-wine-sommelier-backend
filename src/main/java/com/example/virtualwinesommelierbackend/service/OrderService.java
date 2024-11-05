package com.example.virtualwinesommelierbackend.service;

import com.example.virtualwinesommelierbackend.dto.order.OrderDto;
import com.example.virtualwinesommelierbackend.dto.order.OrderRequestDto;
import com.example.virtualwinesommelierbackend.dto.order.OrderStatusDto;
import com.example.virtualwinesommelierbackend.dto.orderitem.OrderItemDto;
import java.util.List;

public interface OrderService {
    OrderDto createOrder(Long userId, OrderRequestDto requestDto);

    List<OrderDto> findAll();

    List<OrderItemDto> getOrderItemsByOrderId(Long orderId);

    OrderItemDto getItemById(Long orderId, Long itemId);

    OrderDto updateStatus(Long orderId, OrderStatusDto statusDto);
}

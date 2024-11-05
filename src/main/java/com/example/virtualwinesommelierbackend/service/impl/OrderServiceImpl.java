package com.example.virtualwinesommelierbackend.service.impl;

import com.example.virtualwinesommelierbackend.dto.order.OrderDto;
import com.example.virtualwinesommelierbackend.dto.order.OrderRequestDto;
import com.example.virtualwinesommelierbackend.dto.order.OrderStatusDto;
import com.example.virtualwinesommelierbackend.dto.orderitem.OrderItemDto;
import com.example.virtualwinesommelierbackend.exception.EntityNotFoundException;
import com.example.virtualwinesommelierbackend.mapper.OrderItemMapper;
import com.example.virtualwinesommelierbackend.mapper.OrderMapper;
import com.example.virtualwinesommelierbackend.model.CartItem;
import com.example.virtualwinesommelierbackend.model.Order;
import com.example.virtualwinesommelierbackend.model.OrderItem;
import com.example.virtualwinesommelierbackend.model.ShoppingCart;
import com.example.virtualwinesommelierbackend.repository.CartItemRepository;
import com.example.virtualwinesommelierbackend.repository.OrderItemRepository;
import com.example.virtualwinesommelierbackend.repository.OrderRepository;
import com.example.virtualwinesommelierbackend.repository.ShoppingCartRepository;
import com.example.virtualwinesommelierbackend.service.OrderService;
import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final ShoppingCartRepository shoppingCartRepository;
    private final CartItemRepository cartItemRepository;
    private final OrderMapper orderMapper;
    private final OrderItemMapper orderItemMapper;

    @Transactional
    @Override
    public OrderDto createOrder(Long userId, OrderRequestDto requestDto) {
        Order order = orderMapper.toEntity(requestDto);
        ShoppingCart shoppingCart = shoppingCartRepository.findByUserId(userId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Shopping cart not found for user id: " + userId));
        order.setUser(shoppingCart.getUser());
        order.setTotal(getTotal(shoppingCart));
        order.setOrderDate(LocalDateTime.now());
        order.setOrderItems(createOrderItems(shoppingCart, order));

        cartItemRepository.deleteAllByShoppingCartId(shoppingCart.getId());
        return orderMapper.toDto(orderRepository.save(order));
    }

    @Override
    public List<OrderDto> findAll() {
        return orderRepository.findAll().stream()
                .map(orderMapper::toDto)
                .toList();
    }

    @Override
    public List<OrderItemDto> getOrderItemsByOrderId(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Order not found with id: "
                        + orderId));
        return order.getOrderItems().stream()
                .map(orderItemMapper::toDto)
                .toList();
    }

    @Override
    public OrderItemDto getItemById(Long orderId, Long itemId) {
        OrderItem orderItem = orderItemRepository.findByIdAndOrderId(itemId, orderId)
                .orElseThrow(() -> new EntityNotFoundException(String.format(
                        "No order item with id: %d for order: %d", itemId, orderId)));
        return orderItemMapper.toDto(orderItem);
    }

    @Override
    public OrderDto updateStatus(Long orderId, OrderStatusDto statusDto) {
        Order order = orderRepository.findById(orderId)
                .map(ord -> {
                    ord.setStatus(statusDto.getStatus());
                    return ord;
                }).orElseThrow(() -> new EntityNotFoundException(("Order not found with id: "
                        + orderId)));
        orderRepository.save(order);
        return orderMapper.toDto(order);
    }

    private BigDecimal getTotal(ShoppingCart shoppingCart) {
        BigDecimal total = BigDecimal.valueOf(0);
        for (CartItem cartItem : shoppingCart.getCartItems()) {
            total = total.add(cartItem.getWine().getPrice());
        }
        return total;
    }

    private Set<OrderItem> createOrderItems(ShoppingCart shoppingCart, Order order) {
        return shoppingCart.getCartItems().stream()
                .map(cartItem -> {
                    OrderItem orderItem = new OrderItem();
                    orderItem.setOrder(order);
                    orderItem.setWine(cartItem.getWine());
                    orderItem.setQuantity(cartItem.getQuantity());
                    orderItem.setPrice(cartItem.getWine().getPrice());
                    return orderItem;
                })
                .collect(Collectors.toSet());
    }
}

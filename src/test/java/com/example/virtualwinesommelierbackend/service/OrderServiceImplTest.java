package com.example.virtualwinesommelierbackend.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.virtualwinesommelierbackend.dto.order.OrderDto;
import com.example.virtualwinesommelierbackend.dto.order.OrderStatusDto;
import com.example.virtualwinesommelierbackend.dto.orderitem.OrderItemDto;
import com.example.virtualwinesommelierbackend.exception.EntityNotFoundException;
import com.example.virtualwinesommelierbackend.mapper.OrderItemMapper;
import com.example.virtualwinesommelierbackend.mapper.OrderMapper;
import com.example.virtualwinesommelierbackend.model.Order;
import com.example.virtualwinesommelierbackend.model.OrderItem;
import com.example.virtualwinesommelierbackend.repository.OrderItemRepository;
import com.example.virtualwinesommelierbackend.repository.OrderRepository;
import com.example.virtualwinesommelierbackend.service.impl.OrderServiceImpl;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class OrderServiceImplTest {

    @Mock
    private OrderRepository orderRepository;
    @Mock
    private OrderItemRepository orderItemRepository;
    @Mock
    private OrderMapper orderMapper;
    @Mock
    private OrderItemMapper orderItemMapper;

    @InjectMocks
    private OrderServiceImpl orderService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testFindAllOrders() {
        Order order = new Order();
        OrderDto orderDto = new OrderDto();

        when(orderRepository.findAll()).thenReturn(Collections.singletonList(order));
        when(orderMapper.toDto(order)).thenReturn(orderDto);

        var result = orderService.findAll();

        verify(orderRepository, times(1)).findAll();

        assertEquals(1, result.size());
        assertEquals(orderDto, result.get(0));
    }

    @Test
    public void testGetOrderItemsByOrderId_OrderNotFound() {
        Long orderId = 1L;

        when(orderRepository.findById(orderId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                () -> orderService.getOrderItemsByOrderId(orderId));
    }

    @Test
    public void testGetOrderItemsByOrderId_Success() {
        Long orderId = 1L;
        Order order = new Order();
        OrderItem orderItem = new OrderItem();
        OrderItemDto orderItemDto = new OrderItemDto();

        order.setOrderItems(Set.of(orderItem));

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
        when(orderItemMapper.toDto(orderItem)).thenReturn(orderItemDto);

        var result = orderService.getOrderItemsByOrderId(orderId);

        verify(orderRepository, times(1)).findById(orderId);

        assertEquals(1, result.size());
        assertEquals(orderItemDto, result.get(0));
    }

    @Test
    public void testGetItemById_ItemNotFound() {
        Long orderId = 1L;
        Long itemId = 2L;

        when(orderItemRepository.findByIdAndOrderId(itemId, orderId))
                .thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                () -> orderService.getItemById(orderId, itemId));
    }

    @Test
    public void testGetItemById_Success() {
        Long orderId = 1L;
        Long itemId = 2L;
        OrderItem orderItem = new OrderItem();
        OrderItemDto orderItemDto = new OrderItemDto();

        when(orderItemRepository.findByIdAndOrderId(itemId, orderId))
                .thenReturn(Optional.of(orderItem));
        when(orderItemMapper.toDto(orderItem)).thenReturn(orderItemDto);

        OrderItemDto result = orderService.getItemById(orderId, itemId);

        verify(orderItemRepository, times(1))
                .findByIdAndOrderId(itemId, orderId);

        assertEquals(orderItemDto, result);
    }

    @Test
    public void testUpdateStatus_OrderNotFound() {
        Long orderId = 1L;
        OrderStatusDto statusDto = new OrderStatusDto();

        when(orderRepository.findById(orderId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                () -> orderService.updateStatus(orderId, statusDto));
    }

    @Test
    public void testUpdateStatus_Success() {
        Long orderId = 1L;
        OrderStatusDto statusDto = new OrderStatusDto();
        statusDto.setStatus(Order.Status.DELIVERED);

        Order order = new Order();

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
        when(orderMapper.toDto(order)).thenReturn(new OrderDto());

        orderService.updateStatus(orderId, statusDto);

        verify(orderRepository, times(1)).save(order);

        assertEquals(Order.Status.DELIVERED, order.getStatus());
    }
}

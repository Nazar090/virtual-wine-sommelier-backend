package com.example.virtualwinesommelierbackend.controller;

import com.example.virtualwinesommelierbackend.dto.order.OrderDto;
import com.example.virtualwinesommelierbackend.dto.order.OrderRequestDto;
import com.example.virtualwinesommelierbackend.dto.orderitem.OrderItemDto;
import com.example.virtualwinesommelierbackend.exception.EntityNotFoundException;
import com.example.virtualwinesommelierbackend.model.User;
import com.example.virtualwinesommelierbackend.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for managing orders in the application.
 * Provides endpoints for creating, retrieving, and updating orders and order items.
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/orders")
@Tag(name = "Orders", description = "Operations related to orders")
public class OrderController {
    private final OrderService orderService;

    /**
     * Endpoint to create and place a new order.
     *
     * @param requestDto the details of the order to be created
     * @return the created OrderDto containing order information
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Place a new order",
            description = "Creates and places a new order in the system")
    public OrderDto createOrder(@RequestBody @Valid OrderRequestDto requestDto) {
        return orderService.createOrder(getUserId(), requestDto);
    }

    /**
     * Endpoint to retrieve all orders in the system.
     *
     * @return a list of OrderDto representing all orders
     */
    @GetMapping
    @Operation(summary = "Get Order Item Details",
            description = "Retrieve the details of a specific item within an order"
                    + " using the order ID and item ID.")
    public List<OrderDto> getAll() {
        return orderService.findAll();
    }

    /**
     * Endpoint to retrieve all items within a specific order.
     *
     * @param orderId the ID of the order whose items are to be retrieved
     * @return a list of OrderItemDto for the specified order
     */
    @GetMapping("/{orderId}/items")
    @Operation(summary = "Get Order Items",
            description = "Retrieve a list of all items within a specific order by order ID.")
    public List<OrderItemDto> getOrderItems(@PathVariable Long orderId) {
        return orderService.getOrderItemsByOrderId(orderId);
    }

    /**
     * Endpoint to retrieve a specific item within an order.
     *
     * @param orderId the ID of the order containing the item
     * @param id the ID of the specific item to retrieve
     * @return an OrderItemDto representing the item details
     */
    @GetMapping("/{orderId}/items/{id}")
    @Operation(summary = "Get Order Item by ID",
            description = "Retrieve a specific item within an order "
                    + "using the order ID and item ID.")
    public OrderItemDto getItemById(@PathVariable Long orderId, @PathVariable Long id) {
        return orderService.getItemById(orderId, id);
    }

    /**
     * Helper method to retrieve the user ID of the currently authenticated user.
     *
     * @return the ID of the currently authenticated user
     * @throws EntityNotFoundException if the user is not found in the authentication context
     */
    private Long getUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            User user = (User) authentication.getPrincipal();
            return user.getId();
        }
        throw new EntityNotFoundException(
                "Can't find authentication name by authentication " + authentication);
    }
}

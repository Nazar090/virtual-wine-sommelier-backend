package com.example.virtualwinesommelierbackend.controller;

import com.example.virtualwinesommelierbackend.dto.order.OrderDto;
import com.example.virtualwinesommelierbackend.dto.user.profile.UserDto;
import com.example.virtualwinesommelierbackend.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/users")
@Tag(name = "Orders", description = "Operations related to users profile")
public class UserController {
    private final OrderService orderService;

    @GetMapping("/{id}")
    @Operation(summary = "Retrieve User Profile Information",
            description = "Retrieve the details of a specific item within an order"
                    + " using the order ID and item ID.")
    public UserDto getUserInfo(@PathVariable Long id) {
        return null;
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
}

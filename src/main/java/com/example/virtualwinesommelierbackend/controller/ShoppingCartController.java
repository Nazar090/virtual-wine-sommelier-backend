package com.example.virtualwinesommelierbackend.controller;

import com.example.virtualwinesommelierbackend.dto.cart.ShoppingCartDto;
import com.example.virtualwinesommelierbackend.dto.cartitem.CartItemQuantityDto;
import com.example.virtualwinesommelierbackend.dto.cartitem.CartItemRequestDto;
import com.example.virtualwinesommelierbackend.exception.EntityNotFoundException;
import com.example.virtualwinesommelierbackend.model.User;
import com.example.virtualwinesommelierbackend.service.ShoppingCartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for managing shopping cart operations.
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/cart")
@Tag(name = "Shopping cart", description = "Operations related to shopping cart")
public class ShoppingCartController {
    private final ShoppingCartService shoppingCartService;

    /**
     * Adds an item to the user's shopping cart.
     *
     * @param cartItemRequestDto Details of the item to add.
     * @return Updated shopping cart details.
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Add a cart item", description = "Add a cart item to shopping cart")
    public ShoppingCartDto addCartItem(
            @RequestBody @Valid CartItemRequestDto cartItemRequestDto) {
        return shoppingCartService.addCartItem(getUserId(), cartItemRequestDto);
    }

    /**
     * Retrieves the user's shopping cart.
     *
     * @return The shopping cart details.
     */
    @GetMapping
    @Operation(summary = "Get a shopping cart", description = "Get a shopping cart")
    public ShoppingCartDto getShoppingCart() {
        return shoppingCartService.getShoppingCartByUserId(getUserId());
    }

    /**
     * Updates the quantity of an item in the shopping cart.
     *
     * @param cartItemId The ID of the cart item to update.
     * @param cartItemQuantityDto The new quantity.
     * @return Updated shopping cart details.
     */
    @PutMapping("/items/{cartItemId}")
    @Operation(summary = "Update a quantity", description = "Update a wine quantity")
    public ShoppingCartDto updateCartItem(
            @PathVariable Long cartItemId,
            @RequestBody @Valid CartItemQuantityDto cartItemQuantityDto) {
        return shoppingCartService.updateProductQuantity(
                getUserId(),
                cartItemId,
                cartItemQuantityDto);
    }

    /**
     * Deletes an item from the shopping cart by its ID.
     *
     * @param cartItemId The ID of the cart item to delete.
     */
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/items/{cartItemId}")
    @Operation(summary = "Delete a cart item", description = "Delete a cart item id")
    public void deleteCartItem(@PathVariable Long cartItemId) {
        shoppingCartService.deleteCartItemById(cartItemId);
    }

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

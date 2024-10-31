package com.example.virtualwinesommelierbackend.service;

import com.example.virtualwinesommelierbackend.dto.cart.ShoppingCartDto;
import com.example.virtualwinesommelierbackend.dto.cartitem.CartItemQuantityDto;
import com.example.virtualwinesommelierbackend.dto.cartitem.CartItemRequestDto;
import com.example.virtualwinesommelierbackend.model.User;

public interface ShoppingCartService {
    ShoppingCartDto getShoppingCartByUserId(Long userId);

    ShoppingCartDto addCartItem(Long userId, CartItemRequestDto cartItemRequestDto);

    ShoppingCartDto updateProductQuantity(Long userId, Long cartItemId,
                                       CartItemQuantityDto cartItemQuantityDto);

    void deleteCartItemById(Long cartItemId);

    void registerNewShoppingCart(User user);
}

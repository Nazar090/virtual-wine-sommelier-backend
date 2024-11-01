package com.example.virtualwinesommelierbackend.dto.cart;

import com.example.virtualwinesommelierbackend.dto.cartitem.CartItemDto;
import java.util.HashSet;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ShoppingCartDto {
    private Long id;
    private Long userId;
    private Set<CartItemDto> cartItems = new HashSet<>();
}

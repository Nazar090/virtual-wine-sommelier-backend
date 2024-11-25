package com.example.virtualwinesommelierbackend.dto.cart;

import com.example.virtualwinesommelierbackend.dto.cartitem.CartItemDto;
import java.util.HashSet;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class ShoppingCartDto {
    private Long id;
    private Long userId;
    private Set<CartItemDto> cartItems = new HashSet<>();
}

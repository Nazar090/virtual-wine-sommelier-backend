package com.example.virtualwinesommelierbackend.dto.cartitem;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CartItemDto {
    private Long id;
    private Long wineId;
    private String wineName;
    private int quantity;
}

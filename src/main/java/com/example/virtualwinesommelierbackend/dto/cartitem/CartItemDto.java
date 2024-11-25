package com.example.virtualwinesommelierbackend.dto.cartitem;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class CartItemDto {
    private Long id;
    private Long wineId;
    private String wineName;
    private int quantity;
}

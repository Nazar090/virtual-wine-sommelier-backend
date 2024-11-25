package com.example.virtualwinesommelierbackend.dto.cartitem;

import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CartItemQuantityDto {
    @Positive(message = "Quantity can has only positive value")
    private int quantity;
}

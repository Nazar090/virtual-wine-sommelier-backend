package com.example.virtualwinesommelierbackend.dto.cartitem;

import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class CartItemQuantityDto {
    @Positive
    private int quantity;
}

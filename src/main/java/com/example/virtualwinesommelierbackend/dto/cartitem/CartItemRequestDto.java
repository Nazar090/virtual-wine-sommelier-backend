package com.example.virtualwinesommelierbackend.dto.cartitem;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CartItemRequestDto {
    @NotNull
    @Positive
    private Long wineId;
    @Positive
    private int quantity;
}

package com.example.virtualwinesommelierbackend.dto.order;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderRequestDto {
    @NotBlank
    @Size(min = 10, max = 55)
    private String shippingAddress;
}

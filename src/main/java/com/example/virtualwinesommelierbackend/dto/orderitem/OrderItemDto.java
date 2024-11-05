package com.example.virtualwinesommelierbackend.dto.orderitem;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderItemDto {
    private Long id;
    private Long wineId;
    private int quantity;
}

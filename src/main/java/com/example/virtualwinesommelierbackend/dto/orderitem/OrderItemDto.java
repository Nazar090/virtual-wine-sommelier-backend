package com.example.virtualwinesommelierbackend.dto.orderitem;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class OrderItemDto {
    private Long id;
    private Long wineId;
    private int quantity;
}

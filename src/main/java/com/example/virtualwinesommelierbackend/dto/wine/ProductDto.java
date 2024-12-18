package com.example.virtualwinesommelierbackend.dto.wine;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ProductDto {
    private int totalProducts;
    private List<WineDto> wineDtos;
}

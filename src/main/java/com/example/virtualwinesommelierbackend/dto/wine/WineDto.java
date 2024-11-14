package com.example.virtualwinesommelierbackend.dto.wine;

import java.math.BigDecimal;

public record WineDto(Long id, String name, String type, String color, String strength,
                      String country, String grape, BigDecimal price, String description) {
}

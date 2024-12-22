package com.example.virtualwinesommelierbackend.dto.wine;

import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class WineDto {
    private Long id;
    private String name;
    private String type;
    private String color;
    private String strength;
    private String country;
    private String grape;
    private BigDecimal price;
    private String description;
}

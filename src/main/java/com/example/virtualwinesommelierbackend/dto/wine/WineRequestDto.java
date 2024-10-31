package com.example.virtualwinesommelierbackend.dto.wine;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WineRequestDto {
    @NotBlank
    private String name;
    @NotBlank
    private String type;
    @NotBlank
    private String strength;
    @NotBlank
    private String country;
    @NotBlank
    private String grape;
    @NotNull
    @Min(0)
    private BigDecimal price;
    @NotBlank
    private String description;
}

package com.example.virtualwinesommelierbackend.dto.wine;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class WineRequestDto {
    @NotBlank(message = "Name is required")
    @Size(max = 100, message = "Name cannot exceed 100 characters")
    private String name;

    @NotBlank(message = "Type is required")
    @Size(max = 50, message = "Type cannot exceed 50 characters")
    private String type;

    @NotBlank(message = "Color is required")
    @Size(max = 50, message = "Color cannot exceed 50 characters")
    private String color;

    @NotBlank(message = "Strength is required")
    @Size(max = 10, message = "Strength cannot exceed 10 characters")
    private String strength;

    @NotBlank(message = "Country is required")
    @Size(max = 50, message = "Country cannot exceed 50 characters")
    private String country;

    @NotBlank(message = "Grape is required")
    @Size(max = 50, message = "Grape cannot exceed 50 characters")
    private String grape;

    @NotNull(message = "Price is required")
    @Min(value = 0, message = "Price must be at least 0")
    @Digits(integer = 10, fraction = 2,
            message = "Price must be a valid monetary value with up to 2 decimal places")
    private BigDecimal price;

    @NotBlank(message = "Description is required")
    @Size(max = 500, message = "Description cannot exceed 500 characters")
    private String description;
}

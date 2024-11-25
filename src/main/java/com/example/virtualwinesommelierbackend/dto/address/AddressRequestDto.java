package com.example.virtualwinesommelierbackend.dto.address;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
@Accessors(chain = true)
public class AddressRequestDto {
    @NotBlank(message = "Street must not be empty")
    @Length(max = 100, message = "Street must not exceed 100 characters")
    private String street;

    @NotBlank(message = "City must not be empty")
    @Length(max = 50, message = "City must not exceed 50 characters")
    private String city;

    @NotBlank(message = "Area must not be empty")
    @Length(max = 50, message = "Area must not exceed 50 characters")
    private String area;

    @NotBlank(message = "ZipCode must not be empty")
    @Length(min = 5, max = 10, message = "ZipCode must be between 5 and 10 characters")
    @Pattern(regexp = "\\d+", message = "ZipCode must contain only numbers")
    private String zipCode;
}

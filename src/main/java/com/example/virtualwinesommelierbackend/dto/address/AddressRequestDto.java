package com.example.virtualwinesommelierbackend.dto.address;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
public class AddressRequestDto {
    @NotBlank
    @Length(max = 100)
    private String street;

    @NotBlank
    @Length(max = 50)
    private String city;

    @NotBlank
    @Length(max = 50)
    private String area;

    @NotBlank
    @Length(min = 5, max = 10)
    private String zipCode;
}

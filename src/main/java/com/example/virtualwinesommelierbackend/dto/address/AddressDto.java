package com.example.virtualwinesommelierbackend.dto.address;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddressDto {
    private String street;
    private String city;
    private String area;
    private String zipCode;
}

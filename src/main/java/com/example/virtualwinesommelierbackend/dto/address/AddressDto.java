package com.example.virtualwinesommelierbackend.dto.address;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class AddressDto {
    private String street;
    private String city;
    private String area;
    private String zipCode;
}

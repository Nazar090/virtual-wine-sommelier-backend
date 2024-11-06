package com.example.virtualwinesommelierbackend.dto.user.profile;

import com.example.virtualwinesommelierbackend.dto.address.AddressDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDto {
    private String firstName;
    private String lastName;
    private String email;
    private AddressDto shippingAddress;
}

package com.example.virtualwinesommelierbackend.dto.user.profile;

import com.example.virtualwinesommelierbackend.dto.address.AddressDto;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Data
@Accessors(chain = true)
public class UserDto {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private AddressDto shippingAddress;
}

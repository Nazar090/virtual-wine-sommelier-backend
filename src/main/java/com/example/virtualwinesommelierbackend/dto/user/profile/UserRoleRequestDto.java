package com.example.virtualwinesommelierbackend.dto.user.profile;

import com.example.virtualwinesommelierbackend.dto.address.AddressDto;
import com.example.virtualwinesommelierbackend.model.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRoleRequestDto {
    private String firstName;
    private String lastName;
    @Email
    @Size(min = 8, max = 35)
    private String email;
    private AddressDto shippingAddress;
    private Role role;
}

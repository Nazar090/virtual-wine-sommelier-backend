package com.example.virtualwinesommelierbackend.dto.user.profile;

import com.example.virtualwinesommelierbackend.dto.address.AddressDto;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRequestDto {
    @NotBlank
    private String firstName;
    @NotBlank
    private String lastName;
    @NotBlank
    @Email
    @Size(min = 8, max = 35)
    private String email;
    @NotNull
    private AddressDto shippingAddress;
}

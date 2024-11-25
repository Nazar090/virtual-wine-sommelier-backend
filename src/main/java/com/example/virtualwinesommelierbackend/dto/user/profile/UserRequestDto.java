package com.example.virtualwinesommelierbackend.dto.user.profile;

import com.example.virtualwinesommelierbackend.dto.address.AddressRequestDto;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class UserRequestDto {
    @NotBlank(message = "First name must not be blank")
    @Size(max = 50, message = "First name must not exceed 50 characters")
    private String firstName;

    @NotBlank(message = "Last name must not be blank")
    @Size(max = 50, message = "Last name must not exceed 50 characters")
    private String lastName;

    @Email(message = "Email should be valid")
    @Size(min = 8, max = 35, message = "Email must be between 8 and 35 characters")
    private String email;

    @NotNull
    private AddressRequestDto shippingAddress;
}

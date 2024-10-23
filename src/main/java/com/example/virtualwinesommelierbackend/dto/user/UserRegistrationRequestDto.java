package com.example.virtualwinesommelierbackend.dto.user;

import com.example.virtualwinesommelierbackend.dto.address.AddressRequestDto;
import com.example.virtualwinesommelierbackend.validation.PasswordMatches;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
@PasswordMatches
public class UserRegistrationRequestDto {
    @NotBlank
    @Email
    private String email;
    @NotBlank
    @Length(min = 8, max = 35)
    @Pattern(
            regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!\\-]).{8,35}$",
            message = "Password must contain at least one digit, one lowercase letter, "
                    + "one uppercase letter, and one special character (@#$%^&+=!-)"
    )
    private String password;
    @NotBlank
    @Length(min = 8, max = 35)
    private String repeatPassword;
    @NotBlank
    private String firstName;
    @NotBlank
    private String lastName;
    @NotNull
    private AddressRequestDto shippingAddress;
}

package com.example.virtualwinesommelierbackend.dto.user;

import com.example.virtualwinesommelierbackend.dto.address.AddressRequestDto;
import com.example.virtualwinesommelierbackend.validation.PasswordMatches;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
@PasswordMatches
@Accessors(chain = true)
public class UserRegistrationRequestDto {
    @NotBlank(message = "Email can't be empty")
    @Email
    private String email;
    @NotBlank(message = "Password can't be empty")
    @Length(min = 8, max = 35)
    @Pattern(
            regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!\\-]).{8,35}$",
            message = "Password must contain at least one digit, one lowercase letter, "
                    + "one uppercase letter, and one special character (@#$%^&+=!-)"
    )
    private String password;
    @NotBlank(message = "You have to repeat your password")
    @Length(min = 8, max = 35)
    private String repeatPassword;
    @NotBlank(message = "Enter your first name")
    private String firstName;
    @NotBlank(message = "Enter your last name")
    private String lastName;
    @NotNull
    private AddressRequestDto shippingAddress;
}

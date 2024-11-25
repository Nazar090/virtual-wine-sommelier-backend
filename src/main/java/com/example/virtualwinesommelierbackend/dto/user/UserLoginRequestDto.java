package com.example.virtualwinesommelierbackend.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserLoginRequestDto(
        @NotBlank(message = "Email can't be empty")
        @Size(min = 8, max = 35)
        @Email
        String email,
        @NotBlank(message = "Password can't be empty")
        @Size(min = 8, max = 35)
        String password
) {
}

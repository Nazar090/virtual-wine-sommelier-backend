package com.example.virtualwinesommelierbackend.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class UserLoginRequestDto {
    @NotBlank(message = "Email can't be empty")
    @Size(min = 8, max = 35)
    @Email
    private String email;
    @NotBlank(message = "Password can't be empty")
    @Size(min = 8, max = 35)
    private String password;
}


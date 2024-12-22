package com.example.virtualwinesommelierbackend.dto.user;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Setter
@Getter
@Accessors(chain = true)
public class UserLoginDto {
    private String token;
}

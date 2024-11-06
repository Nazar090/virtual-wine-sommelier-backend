package com.example.virtualwinesommelierbackend.service;

import com.example.virtualwinesommelierbackend.dto.user.UserRegistrationDto;
import com.example.virtualwinesommelierbackend.dto.user.UserRegistrationRequestDto;
import com.example.virtualwinesommelierbackend.dto.user.profile.UserDto;
import com.example.virtualwinesommelierbackend.exception.RegistrationException;

public interface UserService {
    UserRegistrationDto register(UserRegistrationRequestDto requestDto)
            throws RegistrationException;

    UserDto getUserInfo();
}

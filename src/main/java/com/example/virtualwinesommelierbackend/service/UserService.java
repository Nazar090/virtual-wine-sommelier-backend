package com.example.virtualwinesommelierbackend.service;

import com.example.virtualwinesommelierbackend.dto.user.UserRegistrationDto;
import com.example.virtualwinesommelierbackend.dto.user.UserRegistrationRequestDto;
import com.example.virtualwinesommelierbackend.dto.user.profile.UserDto;
import com.example.virtualwinesommelierbackend.dto.user.profile.UserRequestDto;
import com.example.virtualwinesommelierbackend.dto.user.profile.UserRoleRequestDto;
import com.example.virtualwinesommelierbackend.exception.RegistrationException;
import java.util.List;

public interface UserService {
    UserRegistrationDto register(UserRegistrationRequestDto requestDto)
            throws RegistrationException;

    UserDto getUserInfo(Long userId);

    UserDto updateUserInfo(UserRequestDto requestDto);

    UserDto updateUserRoleInfo(Long id, UserRoleRequestDto requestDto);

    List<UserDto> getAll();
}

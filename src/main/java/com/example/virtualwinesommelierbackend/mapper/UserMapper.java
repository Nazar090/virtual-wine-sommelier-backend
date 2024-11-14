package com.example.virtualwinesommelierbackend.mapper;

import com.example.virtualwinesommelierbackend.config.MapperConfig;
import com.example.virtualwinesommelierbackend.dto.user.UserRegistrationDto;
import com.example.virtualwinesommelierbackend.dto.user.UserRegistrationRequestDto;
import com.example.virtualwinesommelierbackend.dto.user.profile.UserDto;
import com.example.virtualwinesommelierbackend.dto.user.profile.UserRequestDto;
import com.example.virtualwinesommelierbackend.dto.user.profile.UserRoleRequestDto;
import com.example.virtualwinesommelierbackend.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(config = MapperConfig.class, uses = AddressMapper.class)
public interface UserMapper {
    UserRegistrationDto toRegistrationDto(User user);

    UserDto toDto(User user);

    @Mapping(target = "shippingAddress", source = "shippingAddress")
    User toUserEntityFromRegistration(UserRegistrationRequestDto requestDto);

    void updateUserInfo(UserRequestDto requestDto, @MappingTarget User user);

    void updateUserInfo(UserRoleRequestDto requestDto, @MappingTarget User user);
}

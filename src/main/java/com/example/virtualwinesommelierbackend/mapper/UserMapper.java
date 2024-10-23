package com.example.virtualwinesommelierbackend.mapper;

import com.example.virtualwinesommelierbackend.config.MapperConfig;
import com.example.virtualwinesommelierbackend.dto.user.UserRegistrationDto;
import com.example.virtualwinesommelierbackend.dto.user.UserRegistrationRequestDto;
import com.example.virtualwinesommelierbackend.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class, uses = AddressMapper.class)
public interface UserMapper {
    UserRegistrationDto toDto(User user);

    @Mapping(target = "shippingAddress", source = "shippingAddress")
    User toUserEntity(UserRegistrationRequestDto requestDto);
}

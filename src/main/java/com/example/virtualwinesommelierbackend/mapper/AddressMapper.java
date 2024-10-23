package com.example.virtualwinesommelierbackend.mapper;

import com.example.virtualwinesommelierbackend.config.MapperConfig;
import com.example.virtualwinesommelierbackend.dto.address.AddressRequestDto;
import com.example.virtualwinesommelierbackend.model.Address;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfig.class)
public interface AddressMapper {
    Address toAddress(AddressRequestDto addressDto);
}

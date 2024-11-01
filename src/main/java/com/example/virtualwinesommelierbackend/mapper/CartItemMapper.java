package com.example.virtualwinesommelierbackend.mapper;

import com.example.virtualwinesommelierbackend.config.MapperConfig;
import com.example.virtualwinesommelierbackend.dto.cartitem.CartItemDto;
import com.example.virtualwinesommelierbackend.dto.cartitem.CartItemRequestDto;
import com.example.virtualwinesommelierbackend.model.CartItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class)
public interface CartItemMapper {
    @Mapping(target = "wineId", source = "wine.id")
    @Mapping(target = "wineName", source = "wine.name")
    CartItemDto toDto(CartItem cartItem);

    @Mapping(target = "wine.id", source = "wineId")
    CartItem toEntity(CartItemRequestDto requestDto);
}

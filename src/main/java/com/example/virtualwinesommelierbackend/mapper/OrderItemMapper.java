package com.example.virtualwinesommelierbackend.mapper;

import com.example.virtualwinesommelierbackend.config.MapperConfig;
import com.example.virtualwinesommelierbackend.dto.orderitem.OrderItemDto;
import com.example.virtualwinesommelierbackend.model.OrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class)
public interface OrderItemMapper {
    @Mapping(target = "wineId", source = "wine.id")
    OrderItemDto toDto(OrderItem orderItem);
}

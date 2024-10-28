package com.example.virtualwinesommelierbackend.mapper;

import com.example.virtualwinesommelierbackend.config.MapperConfig;
import com.example.virtualwinesommelierbackend.dto.wine.WineDto;
import com.example.virtualwinesommelierbackend.dto.wine.WineRequestDto;
import com.example.virtualwinesommelierbackend.model.Wine;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(config = MapperConfig.class)
public interface WineMapper {
    WineDto toDto(Wine wine);

    Wine toEntity(WineRequestDto requestDto);

    void updateWineFromDto(WineRequestDto requestDto, @MappingTarget Wine wine);
}

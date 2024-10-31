package com.example.virtualwinesommelierbackend.service;

import com.example.virtualwinesommelierbackend.dto.wine.WineDto;
import com.example.virtualwinesommelierbackend.dto.wine.WineRequestDto;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface WineService {
    WineDto save(WineRequestDto requestDto);

    WineDto getById(Long id);

    List<WineDto> getAll(Pageable pageable);

    WineDto update(Long id, WineRequestDto requestDto);

    void deleteById(Long id);
}

package com.example.virtualwinesommelierbackend.service;

import com.example.virtualwinesommelierbackend.dto.wine.ProductDto;
import com.example.virtualwinesommelierbackend.dto.wine.WineDto;
import com.example.virtualwinesommelierbackend.dto.wine.WineRequestDto;
import org.springframework.web.multipart.MultipartFile;

public interface WineService {
    WineDto save(WineRequestDto requestDto);

    WineDto getById(Long id);

    ProductDto getAll();

    WineDto update(Long id, WineRequestDto requestDto);

    void deleteById(Long id);

    WineDto uploadFile(Long id, MultipartFile file);
}

package com.example.virtualwinesommelierbackend.service.impl;

import com.example.virtualwinesommelierbackend.dto.wine.WineDto;
import com.example.virtualwinesommelierbackend.dto.wine.WineRequestDto;
import com.example.virtualwinesommelierbackend.exception.EntityNotFoundException;
import com.example.virtualwinesommelierbackend.mapper.WineMapper;
import com.example.virtualwinesommelierbackend.model.Wine;
import com.example.virtualwinesommelierbackend.repository.WineRepository;
import com.example.virtualwinesommelierbackend.service.WineService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WineServiceImpl implements WineService {
    private final WineRepository wineRepository;
    private final WineMapper wineMapper;

    @Override
    public WineDto save(WineRequestDto requestDto) {
        Wine wine = wineMapper.toEntity(requestDto);
        return wineMapper.toDto(wineRepository.save(wine));
    }

    @Override
    public WineDto getById(Long id) {
        return wineMapper.toDto(wineRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Can't find wine by id: " + id)));
    }

    @Override
    public List<WineDto> getAll(Pageable pageable) {
        return wineRepository.findAll(pageable).stream()
                .map(wineMapper::toDto)
                .toList();
    }

    @Override
    public void update(Long id, WineRequestDto requestDto) {
        Wine wine = wineRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                "Can't find wine by id: " + id));
        wineMapper.updateWineFromDto(requestDto, wine);
        wineMapper.toDto(wineRepository.save(wine));
    }

    @Override
    public void deleteById(Long id) {
        wineRepository.deleteById(id);
    }
}

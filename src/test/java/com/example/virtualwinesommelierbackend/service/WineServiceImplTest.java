package com.example.virtualwinesommelierbackend.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.virtualwinesommelierbackend.dto.wine.ProductDto;
import com.example.virtualwinesommelierbackend.dto.wine.WineDto;
import com.example.virtualwinesommelierbackend.dto.wine.WineRequestDto;
import com.example.virtualwinesommelierbackend.exception.EntityNotFoundException;
import com.example.virtualwinesommelierbackend.mapper.WineMapper;
import com.example.virtualwinesommelierbackend.model.Wine;
import com.example.virtualwinesommelierbackend.repository.OrderItemRepository;
import com.example.virtualwinesommelierbackend.repository.WineRepository;
import com.example.virtualwinesommelierbackend.service.impl.WineServiceImpl;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class WineServiceImplTest {
    @Mock
    private WineRepository wineRepository;

    @Mock
    private OrderItemRepository orderItemRepository;

    @Mock
    private WineMapper wineMapper;

    @InjectMocks
    private WineServiceImpl wineService;

    private Wine wine;
    private WineDto wineDto;
    private WineRequestDto wineRequestDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Sample data setup
        wine = new Wine();
        wine.setId(1L);

        wineDto = new WineDto()
                .setId(1L)
                .setName("Sample Wine")
                .setType("Dry")
                .setColor("Red")
                .setStrength("13%")
                .setCountry("France")
                .setPrice(BigDecimal.valueOf(20.00))
                .setDescription("Description here");

        wineRequestDto = new WineRequestDto();
    }

    @Test
    void testSave() {
        when(wineMapper.toEntity(any(WineRequestDto.class))).thenReturn(wine);
        when(wineRepository.save(any(Wine.class))).thenReturn(wine);
        when(wineMapper.toDto(any(Wine.class))).thenReturn(wineDto);

        WineDto result = wineService.save(wineRequestDto);

        assertNotNull(result);
        assertEquals(wineDto, result);

        verify(wineMapper).toEntity(wineRequestDto);
        verify(wineRepository).save(wine);
        verify(wineMapper).toDto(wine);
    }

    @Test
    void testGetById_WineFound() {
        when(wineRepository.findById(1L)).thenReturn(Optional.of(wine));
        when(wineMapper.toDto(wine)).thenReturn(wineDto);

        WineDto result = wineService.getById(1L);

        assertNotNull(result);
        assertEquals(wineDto, result);

        verify(wineRepository).findById(1L);
        verify(wineMapper).toDto(wine);
    }

    @Test
    void testGetById_WineNotFound() {
        when(wineRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> wineService.getById(1L));

        verify(wineRepository).findById(1L);
        verify(wineMapper, never()).toDto(any());
    }

    @Test
    void testGetAll() {
        List<Wine> wines = List.of(wine);
        when(wineRepository.findAll()).thenReturn(wines);
        when(wineMapper.toDto(wine)).thenReturn(wineDto);

        ProductDto result = wineService.getAll();

        assertNotNull(result);
        assertEquals(1, result.getWineDtos().size());
        assertEquals(wineDto, result.getWineDtos().get(0));

        verify(wineRepository).findAll();
        verify(wineMapper).toDto(wine);
    }

    @Test
    void testUpdate_WineFound() {
        when(wineRepository.findById(1L)).thenReturn(Optional.of(wine));
        when(wineMapper.toDto(wine)).thenReturn(wineDto);
        when(wineRepository.save(wine)).thenReturn(wine);

        WineDto result = wineService.update(1L, wineRequestDto);

        assertNotNull(result);
        assertEquals(wineDto, result);

        verify(wineRepository).findById(1L);
        verify(wineMapper).updateWineFromDto(wineRequestDto, wine);
        verify(wineRepository).save(wine);
        verify(wineMapper).toDto(wine);
    }

    @Test
    void testUpdate_WineNotFound() {
        when(wineRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> wineService.update(1L, wineRequestDto));

        verify(wineRepository).findById(1L);
        verify(wineMapper, never()).updateWineFromDto(any(), any());
        verify(wineRepository, never()).save(any());
    }

    @Test
    void testDeleteWineById() {
        wineService.deleteById(1L);

        verify(wineRepository).deleteById(1L);
    }
}

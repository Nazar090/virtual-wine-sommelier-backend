package com.example.virtualwinesommelierbackend.service.impl;

import com.example.virtualwinesommelierbackend.dto.wine.ProductDto;
import com.example.virtualwinesommelierbackend.dto.wine.WineDto;
import com.example.virtualwinesommelierbackend.dto.wine.WineRequestDto;
import com.example.virtualwinesommelierbackend.exception.EntityNotFoundException;
import com.example.virtualwinesommelierbackend.mapper.WineMapper;
import com.example.virtualwinesommelierbackend.model.Wine;
import com.example.virtualwinesommelierbackend.repository.OrderItemRepository;
import com.example.virtualwinesommelierbackend.repository.WineRepository;
import com.example.virtualwinesommelierbackend.service.WineService;
import jakarta.transaction.Transactional;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Wine Service Implementation focused on business management wine's data from DB.
 */
@Service
@RequiredArgsConstructor
public class WineServiceImpl implements WineService {
    private final WineRepository wineRepository;
    private final OrderItemRepository orderItemRepository;
    private final WineMapper wineMapper;

    /**
     * Save product in DB, only for users with a higher role than 'USER'.
     *
     * @param requestDto input data to save the new product in the DB.
     * @return just saved product in DB.
     */
    @Override
    public WineDto save(WineRequestDto requestDto) {
        Wine wine = wineMapper.toEntity(requestDto);
        return wineMapper.toDto(wineRepository.save(wine));
    }

    /**
     * Retrieves a product by its ID.
     *
     * @param id The path variable to find by it the certain product.
     * @return product that you found by its ID.
     */
    @Override
    public WineDto getById(Long id) {
        return wineMapper.toDto(wineRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Can't find wine by id: " + id)));
    }

    /**
     * Retrieves all products form the DB with pagination and sorting options.
     *
     * @return a ProductDto objects representing the wine products in the system + their total count
     */
    @Override
    public ProductDto getAll() {
        ProductDto product = new ProductDto();
        List<Wine> wines = wineRepository.findAll();
        product.setWineDtos(wines.stream().map(wineMapper::toDto).toList());
        product.setTotalProducts(wines.size());
        return product;
    }

    /**
     * Update the currently saved product in DB, only for users with a higher role
     * than 'USER'.
     *
     * @param id The path variable to find by it the certain product.
     * @param requestDto input data to update the product in the DB.
     * @return just updated product.
     */
    @Override
    public WineDto update(Long id, WineRequestDto requestDto) {
        Wine wine = wineRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                "Can't find wine by id: " + id));
        wineMapper.updateWineFromDto(requestDto, wine);
        return wineMapper.toDto(wineRepository.save(wine));
    }

    /**
     * Delete the product by its id.
     * @param id The path variable to find and delete by it the certain product.
     */
    @Override
    @Transactional
    public void deleteById(Long id) {
        orderItemRepository.deleteByWineId(id);

        wineRepository.deleteById(id);
    }
}

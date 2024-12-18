package com.example.virtualwinesommelierbackend.controller;

import com.example.virtualwinesommelierbackend.dto.wine.ProductDto;
import com.example.virtualwinesommelierbackend.dto.wine.WineDto;
import com.example.virtualwinesommelierbackend.service.WineService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller responsible for retrieving products for users.
 * Possible to get all products(wines) and get a product by ID.
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/products")
@Tag(name = "Products", description = "Operations related to products")
public class ProductController {
    private final WineService wineService;

    /**
     * Method responsible for retrieving a product by ID.
     *
     * @param id It indicates the id to get the product.
     * @return The saved product details.
     */
    @GetMapping("/{id}")
    @Operation(summary = "Get a wine by ID", description = "Retrieve a specific wine by its ID")
    public WineDto findById(@PathVariable Long id) {
        return wineService.getById(id);
    }

    /**
     * Method responsible for retrieves a paginated and sorted list of all wines.
     *
     * @return a ProductDto objects representing the wine data that matches the
     *         pagination and sorting criteria provided.
     */
    @GetMapping
    @Operation(summary = "Get all wines",
            description = "Retrieve a list of all wines and count of them")
    public ProductDto findAll() {
        return wineService.getAll();
    }
}

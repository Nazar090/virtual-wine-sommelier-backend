package com.example.virtualwinesommelierbackend.controller;

import com.example.virtualwinesommelierbackend.dto.order.OrderDto;
import com.example.virtualwinesommelierbackend.dto.order.OrderStatusDto;
import com.example.virtualwinesommelierbackend.dto.user.profile.UserDto;
import com.example.virtualwinesommelierbackend.dto.user.profile.UserRoleRequestDto;
import com.example.virtualwinesommelierbackend.dto.wine.WineDto;
import com.example.virtualwinesommelierbackend.dto.wine.WineRequestDto;
import com.example.virtualwinesommelierbackend.service.OrderService;
import com.example.virtualwinesommelierbackend.service.UserService;
import com.example.virtualwinesommelierbackend.service.WineService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * Controller responsible for handling administrative operations,
 * such as managing wine products in the catalog, handling orders, and
 * managing user roles and profiles.
 *
 * This controller provides endpoints for admins to add, update, and delete
 * products, view and update orders, and manage user accounts.
 *
 * All endpoints require administrative privileges.
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/admin")
@Tag(name = "Admin panel", description = "Operations related to users profile")
public class AdminController {
    private final WineService wineService;
    private final OrderService orderService;
    private final UserService userService;

    // Catalog

    /**
     * Adds a new wine product to the catalog.
     * Only accessible by admins.
     *
     * @param requestDto contains information about the wine to add.
     * @return the saved WineDto with details of the added wine.
     */
    @PostMapping("/catalog")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Operation(summary = "Add Product",
            description = "Adds a new wine product to the catalog")
    public WineDto addProduct(@RequestBody @Valid WineRequestDto requestDto) {
        return wineService.save(requestDto);
    }

    /**
     * Uploads an image for the specified wine product.
     * This endpoint allows adding or updating the image of a wine product.
     *
     * @param id   the ID of the wine product to which the image will be associated.
     * @param file the image file to upload, provided as a multipart/form-data request.
     * @return the updated WineDto object containing details of the wine product,
     *         including the new image URL.
     */
    @PostMapping("/products/{id}/image")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Add Product",
            description = "Adds a new wine product to the catalog")
    public WineDto uploadProductImage(@PathVariable Long id,
                                      @RequestParam("file") MultipartFile file) {
        return wineService.uploadFile(id, file);
    }

    /**
     * Updates an existing wine product in the catalog.
     * Only accessible by admins.
     *
     * @param id the ID of the wine product to update.
     * @param requestDto contains updated information about the wine.
     * @return the updated WineDto with details of the wine.
     */
    @PutMapping("/catalog/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Operation(summary = "Update Product",
            description = "Updates an existing wine product in the catalog")
    public WineDto updateProduct(@PathVariable Long id,
                                 @RequestBody @Valid WineRequestDto requestDto) {
        return wineService.update(id, requestDto);
    }

    /**
     * Deletes a wine product from the catalog.
     * Only accessible by admins.
     *
     * @param id the ID of the wine product to delete.
     */
    @DeleteMapping("/catalog/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Operation(summary = "Delete Product",
            description = "Deletes a wine product from the catalog")
    public void deleteProduct(@PathVariable Long id) {
        wineService.deleteById(id);
    }

    // Orders

    /**
     * Retrieves a list of all orders.
     * Only accessible by admins.
     *
     * @return a list of OrderDto representing all orders.
     */
    @GetMapping("/orders")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Operation(summary = "Get All Orders",
            description = "Retrieves a list of all orders")
    public List<OrderDto> getAllOrders() {
        return orderService.findAll();
    }

    /**
     * Updates the status of a specific order.
     * Only accessible by admins.
     *
     * @param id the ID of the order to update.
     * @param statusDto contains the new status for the order.
     * @return the updated OrderDto with details of the order.
     */
    @PutMapping("/orders/{id}/status")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Operation(summary = "Update Order Status",
            description = "Updates the status of a specific order")
    public OrderDto updateOrderStatus(@PathVariable Long id,
                                      @RequestBody @Valid OrderStatusDto statusDto) {
        return orderService.updateStatus(id, statusDto);
    }

    // Users

    /**
     * Retrieves a list of all users.
     * Only accessible by admins.
     *
     * @return a list of UserDto representing all users.
     */
    @GetMapping("/users")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Operation(summary = "Get All Users",
            description = "Retrieves a list of all users")
    public List<UserDto> getAllUsers() {
        return userService.getAll();
    }

    /**
     * Updates the role information of a specific user.
     * Only accessible by admins.
     *
     * @param id the ID of the user to update.
     * @param requestDto contains the new role information for the user.
     * @return the updated UserDto with details of the user.
     */
    @PutMapping("/users/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Operation(summary = "Update User Role",
            description = "Updates the role information of a specific user")
    public UserDto updateDetails(@PathVariable Long id,
                                 @RequestBody @Valid UserRoleRequestDto requestDto) {
        return userService.updateUserRoleInfo(id, requestDto);
    }
}

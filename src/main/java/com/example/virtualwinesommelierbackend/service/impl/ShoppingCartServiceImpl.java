package com.example.virtualwinesommelierbackend.service.impl;

import com.example.virtualwinesommelierbackend.dto.cart.ShoppingCartDto;
import com.example.virtualwinesommelierbackend.dto.cartitem.CartItemQuantityDto;
import com.example.virtualwinesommelierbackend.dto.cartitem.CartItemRequestDto;
import com.example.virtualwinesommelierbackend.exception.EntityNotFoundException;
import com.example.virtualwinesommelierbackend.mapper.CartItemMapper;
import com.example.virtualwinesommelierbackend.mapper.ShoppingCartMapper;
import com.example.virtualwinesommelierbackend.model.CartItem;
import com.example.virtualwinesommelierbackend.model.ShoppingCart;
import com.example.virtualwinesommelierbackend.model.User;
import com.example.virtualwinesommelierbackend.model.Wine;
import com.example.virtualwinesommelierbackend.repository.CartItemRepository;
import com.example.virtualwinesommelierbackend.repository.ShoppingCartRepository;
import com.example.virtualwinesommelierbackend.repository.WineRepository;
import com.example.virtualwinesommelierbackend.service.ShoppingCartService;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Service implementation for managing shopping cart operations.
 * This class provides business logic to manage shopping carts.
 */
@Service
@RequiredArgsConstructor
public class ShoppingCartServiceImpl implements ShoppingCartService {
    private final ShoppingCartRepository shoppingCartRepository;
    private final CartItemRepository cartItemRepository;
    private final WineRepository wineRepository;
    private final ShoppingCartMapper shoppingCartMapper;
    private final CartItemMapper cartItemMapper;

    /**
     * Retrieves the shopping cart for a specific user.
     *
     * @param userId the ID of the user who retrieves this cart
     * @return the user's shopping cart as DTO
     */
    @Override
    public ShoppingCartDto getShoppingCartByUserId(Long userId) {
        return shoppingCartMapper.toDto(shoppingCartRepository.findByUserId(userId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Can not find shoping cart with id: "
                                + userId)));
    }

    /**
     * Add a new item to the user's cart. If the item already exists, updates its quantity.
     *
     * @param userId the ID of the user who adds a new item to the cart.
     * @param cartItemRequestDto the details of the item to add
     * @return the updated user's shopping cart as DTO
     */
    @Override
    public ShoppingCartDto addCartItem(Long userId, CartItemRequestDto cartItemRequestDto) {
        ShoppingCart shoppingCart = shoppingCartRepository.findByUserId(userId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Can not find shopping cart with id: "
                                + userId));

        // Check if the item already exists in the cart, and update quantity if it does
        Optional<CartItem> existingCartItem = shoppingCart.getCartItems().stream()
                .filter(item -> item.getWine().getId().equals(cartItemRequestDto.getWineId()))
                .findFirst();
        CartItem cartItem;
        if (existingCartItem.isPresent()) {
            cartItem = existingCartItem.get();
            cartItem.setQuantity(cartItemRequestDto.getQuantity());
        } else {
            cartItem = createNewCartItem(cartItemRequestDto, shoppingCart);
        }
        cartItemRepository.save(cartItem);
        return shoppingCartMapper.toDto(shoppingCart);
    }

    /**
     * Updates the quantity of an existing product in the user's cart.
     *
     * @param userId the ID of the user
     * @param cartItemId the ID of the cart item to update
     * @param cartItemQuantityDto contains the new quantity
     * @return the updated shopping cart as a DTO
     */
    @Override
    public ShoppingCartDto updateProductQuantity(Long userId, Long cartItemId,
                                              CartItemQuantityDto cartItemQuantityDto) {
        ShoppingCart cart = shoppingCartRepository.findByUserId(userId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Can not find shopping cart with id: "
                                + userId));

        // Find the specific cart item and update its quantity
        CartItem cartItem = cartItemRepository.findByIdAndShoppingCartId(cartItemId, cart.getId())
                .map(item -> {
                    item.setQuantity(cartItemQuantityDto.getQuantity());
                    return item;
                }).orElseThrow(() -> new EntityNotFoundException(
                        String.format("No cart item with id: %d for user: %d", cartItemId, userId)
                ));
        cartItemRepository.save(cartItem);
        return shoppingCartMapper.toDto(cart);
    }

    /**
     * Deletes a cart item by its ID.
     *
     * @param cartItemId the ID of the cart item to delete
     */
    @Override
    public void deleteCartItemById(Long cartItemId) {
        cartItemRepository.deleteById(cartItemId);
    }

    /**
     * Registers a new shopping cart for a user. This is typically used during user registration.
     *
     * @param user the user for whom the shopping cart is to be created
     */
    @Override
    public void registerNewShoppingCart(User user) {
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setUser(user);
        shoppingCartRepository.save(shoppingCart);
    }

    /**
     * Helper method to create a new cart item for a specific wine and shopping cart.
     *
     * @param cartItemRequestDto the details of the item to add
     * @param shoppingCart the shopping cart to which the item will be added
     * @return the newly created cart item
     */
    private CartItem createNewCartItem(CartItemRequestDto cartItemRequestDto,
                                       ShoppingCart shoppingCart) {
        Wine wine = wineRepository.findById(cartItemRequestDto.getWineId())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Can not find wine with id: "
                                + cartItemRequestDto.getWineId()));

        CartItem cartItem = cartItemMapper.toEntity(cartItemRequestDto);
        cartItem.setWine(wine);
        cartItem.setShoppingCart(shoppingCart);
        return cartItem;
    }
}

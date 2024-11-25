package com.example.virtualwinesommelierbackend.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
import com.example.virtualwinesommelierbackend.service.impl.ShoppingCartServiceImpl;
import java.math.BigDecimal;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class ShoppingCartServiceImplTest {

    @Mock
    private ShoppingCartRepository shoppingCartRepository;

    @Mock
    private CartItemRepository cartItemRepository;

    @Mock
    private WineRepository wineRepository;

    @Mock
    private ShoppingCartMapper shoppingCartMapper;

    @Mock
    private CartItemMapper cartItemMapper;

    @InjectMocks
    private ShoppingCartServiceImpl shoppingCartService;

    private User user;
    private ShoppingCart shoppingCart;
    private CartItem cartItem;
    private Wine wine;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        user = new User();
        user.setId(1L);
        shoppingCart = new ShoppingCart();
        shoppingCart.setId(1L);
        shoppingCart.setUser(user);
        cartItem = new CartItem();
        cartItem.setId(1L);
        cartItem.setQuantity(1);
        wine = new Wine();
        wine.setId(1L);
        wine.setPrice(BigDecimal.valueOf(100));
    }

    @Test
    void testGetShoppingCartByUserId_Success() {
        when(shoppingCartRepository.findByUserId(1L)).thenReturn(Optional.of(shoppingCart));
        ShoppingCartDto shoppingCartDto = new ShoppingCartDto();
        when(shoppingCartMapper.toDto(shoppingCart)).thenReturn(shoppingCartDto);

        ShoppingCartDto result = shoppingCartService.getShoppingCartByUserId(1L);

        assertEquals(shoppingCartDto, result);
        verify(shoppingCartRepository).findByUserId(1L);
        verify(shoppingCartMapper).toDto(shoppingCart);
    }

    @Test
    void testAddCartItem_ItemExists() {
        CartItemRequestDto requestDto = new CartItemRequestDto();
        requestDto.setWineId(1L);
        requestDto.setQuantity(2);
        cartItem.setWine(wine);
        cartItem.setQuantity(1);

        shoppingCart.setCartItems(Set.of(cartItem));
        when(shoppingCartRepository.findByUserId(1L)).thenReturn(Optional.of(shoppingCart));
        when(cartItemRepository.save(cartItem)).thenReturn(cartItem);

        ShoppingCartDto shoppingCartDto = new ShoppingCartDto();
        when(shoppingCartMapper.toDto(shoppingCart)).thenReturn(shoppingCartDto);

        ShoppingCartDto result = shoppingCartService.addCartItem(1L, requestDto);

        assertEquals(shoppingCartDto, result);
        assertEquals(2, cartItem.getQuantity());
        verify(cartItemRepository).save(cartItem);
    }

    @Test
    void testAddCartItem_NewItem() {
        CartItemRequestDto requestDto = new CartItemRequestDto();
        requestDto.setWineId(1L);
        requestDto.setQuantity(1);

        when(shoppingCartRepository.findByUserId(1L)).thenReturn(Optional.of(shoppingCart));
        when(wineRepository.findById(1L)).thenReturn(Optional.of(wine));

        CartItem newCartItem = new CartItem();
        when(cartItemMapper.toEntity(requestDto)).thenReturn(newCartItem);
        ShoppingCartDto shoppingCartDto = new ShoppingCartDto();
        when(shoppingCartMapper.toDto(shoppingCart)).thenReturn(shoppingCartDto);

        ShoppingCartDto result = shoppingCartService.addCartItem(1L, requestDto);

        assertEquals(shoppingCartDto, result);
        verify(cartItemRepository).save(newCartItem);
        assertEquals(wine, newCartItem.getWine());;
    }

    @Test
    void testUpdateProductQuantity_Success() {
        CartItemQuantityDto quantityDto = new CartItemQuantityDto();
        quantityDto.setQuantity(3);

        when(shoppingCartRepository.findByUserId(1L)).thenReturn(Optional.of(shoppingCart));
        when(cartItemRepository.findByIdAndShoppingCartId(1L, 1L))
                .thenReturn(Optional.of(cartItem));
        ShoppingCartDto shoppingCartDto = new ShoppingCartDto();
        when(shoppingCartMapper.toDto(shoppingCart)).thenReturn(shoppingCartDto);

        ShoppingCartDto result = shoppingCartService.updateProductQuantity(1L, 1L, quantityDto);

        assertEquals(shoppingCartDto, result);
        assertEquals(3, cartItem.getQuantity());
        verify(cartItemRepository).save(cartItem);
    }

    @Test
    void testUpdateProductQuantity_NotFound() {
        CartItemQuantityDto quantityDto = new CartItemQuantityDto();
        quantityDto.setQuantity(3);

        when(shoppingCartRepository.findByUserId(1L)).thenReturn(Optional.of(shoppingCart));
        when(cartItemRepository.findByIdAndShoppingCartId(1L, 1L))
                .thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                () -> shoppingCartService.updateProductQuantity(
                        1L,
                        1L,
                        quantityDto
                ));
    }

    @Test
    void testDeleteCartItemById() {
        shoppingCartService.deleteCartItemById(1L);
        verify(cartItemRepository).deleteById(1L);
    }

    @Test
    void testRegisterNewShoppingCart() {
        shoppingCartService.registerNewShoppingCart(user);
        ArgumentCaptor<ShoppingCart> captor = ArgumentCaptor.forClass(ShoppingCart.class);
        verify(shoppingCartRepository).save(captor.capture());
        assertEquals(user, captor.getValue().getUser());
    }
}

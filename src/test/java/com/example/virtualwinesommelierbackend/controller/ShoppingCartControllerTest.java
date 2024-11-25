package com.example.virtualwinesommelierbackend.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.virtualwinesommelierbackend.dto.cart.ShoppingCartDto;
import com.example.virtualwinesommelierbackend.dto.cartitem.CartItemDto;
import com.example.virtualwinesommelierbackend.dto.cartitem.CartItemQuantityDto;
import com.example.virtualwinesommelierbackend.dto.cartitem.CartItemRequestDto;
import com.example.virtualwinesommelierbackend.security.AuthenticationService;
import com.example.virtualwinesommelierbackend.security.JwtUtil;
import com.example.virtualwinesommelierbackend.service.ShoppingCartService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Set;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ShoppingCartControllerTest {
    protected static MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private JwtUtil jwtUtil;

    @MockBean
    private ShoppingCartService shoppingCartService;

    @MockBean
    private AuthenticationService authService;

    @BeforeAll
    static void beforeAll(
            @Autowired WebApplicationContext applicationContext
    ) {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(applicationContext)
                .apply(springSecurity())
                .build();
    }

    @Test
    @WithMockUser(roles = "USER")
    void addCartItem_ShouldReturnUpdatedCart() throws Exception {
        CartItemRequestDto requestDto = new CartItemRequestDto()
                .setWineId(4L)
                .setQuantity(3);

        ShoppingCartDto expectedCart = new ShoppingCartDto()
                .setId(1L)
                .setUserId(1L)
                .setCartItems(Set.of(new CartItemDto()
                        .setId(1L)
                        .setWineId(4L)
                        .setWineName("Light Apollo")
                        .setQuantity(1)
                ));

        Mockito.when(authService.getUserId()).thenReturn(1L);
        Mockito.when(shoppingCartService.addCartItem(eq(1L), any(CartItemRequestDto.class)))
                .thenReturn(expectedCart);

        String jsonRequest = objectMapper.writeValueAsString(requestDto);

        MvcResult result = mockMvc.perform(post("/api/cart")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isCreated())
                .andReturn();

        ShoppingCartDto actualCart = objectMapper.readValue(result.getResponse()
                .getContentAsString(), ShoppingCartDto.class);

        Assertions.assertNotNull(actualCart);
        Assertions.assertEquals(expectedCart.getId(), actualCart.getId());
        Assertions.assertEquals(expectedCart.getCartItems().size(), actualCart.getCartItems()
                .size());
        Assertions.assertEquals(expectedCart.getUserId(), actualCart.getUserId());
    }

    @Test
    @WithMockUser(roles = "USER")
    void getShoppingCart_ShouldReturnCart() throws Exception {
        ShoppingCartDto expectedCart = new ShoppingCartDto()
                .setId(1L)
                .setUserId(1L)
                .setCartItems(Set.of(new CartItemDto()
                        .setId(1L)
                        .setWineId(2L)
                        .setWineName("Red Cherry")
                        .setQuantity(1)
                ));

        Mockito.when(authService.getUserId()).thenReturn(1L);
        Mockito.when(shoppingCartService.getShoppingCartByUserId(1L)).thenReturn(expectedCart);

        MvcResult result = mockMvc.perform(get("/api/cart"))
                .andExpect(status().isOk())
                .andReturn();

        ShoppingCartDto actualCart = objectMapper.readValue(result.getResponse()
                .getContentAsString(), ShoppingCartDto.class);

        Assertions.assertNotNull(actualCart);
        Assertions.assertEquals(expectedCart.getId(), actualCart.getId());
        Assertions.assertEquals(expectedCart.getCartItems().size(), actualCart.getCartItems()
                .size());
        Assertions.assertEquals(expectedCart.getUserId(), actualCart.getUserId());
    }

    @Test
    @WithMockUser(roles = "USER")
    void updateCartItem_ShouldReturnUpdatedCart() throws Exception {
        CartItemQuantityDto quantityDto = new CartItemQuantityDto();
        quantityDto.setQuantity(5);
        ShoppingCartDto expectedCart = new ShoppingCartDto()
                .setId(1L)
                .setUserId(1L)
                .setCartItems(Set.of(new CartItemDto()
                        .setId(1L)
                        .setWineId(3L)
                        .setWineName("Black Dragon")
                        .setQuantity(3)
                ));

        Mockito.when(authService.getUserId()).thenReturn(1L);
        Mockito.when(shoppingCartService.updateProductQuantity(eq(1L), eq(1L),
                any(CartItemQuantityDto.class))).thenReturn(expectedCart);

        String jsonRequest = objectMapper.writeValueAsString(quantityDto);

        MvcResult result = mockMvc.perform(put("/api/cart/items/{cartItemId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isOk())
                .andReturn();

        ShoppingCartDto actualCart = objectMapper.readValue(result.getResponse()
                .getContentAsString(), ShoppingCartDto.class);

        Assertions.assertNotNull(actualCart);
        Assertions.assertEquals(expectedCart.getId(), actualCart.getId());
        Assertions.assertEquals(expectedCart.getCartItems().size(),
                actualCart.getCartItems().size());
    }

    @Test
    @WithMockUser(roles = "USER")
    void deleteCartItem_ShouldReturnNoContent() throws Exception {
        Mockito.doNothing().when(shoppingCartService).deleteCartItemById(1L);

        mockMvc.perform(delete("/api/cart/items/{cartItemId}", 1L))
                .andExpect(status().isNoContent());
    }
}

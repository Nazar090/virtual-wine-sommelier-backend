package com.example.virtualwinesommelierbackend.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.virtualwinesommelierbackend.dto.address.AddressDto;
import com.example.virtualwinesommelierbackend.dto.address.AddressRequestDto;
import com.example.virtualwinesommelierbackend.dto.order.OrderDto;
import com.example.virtualwinesommelierbackend.dto.orderitem.OrderItemDto;
import com.example.virtualwinesommelierbackend.dto.user.profile.UserDto;
import com.example.virtualwinesommelierbackend.dto.user.profile.UserRequestDto;
import com.example.virtualwinesommelierbackend.model.Order;
import com.example.virtualwinesommelierbackend.security.AuthenticationService;
import com.example.virtualwinesommelierbackend.security.JwtUtil;
import com.example.virtualwinesommelierbackend.service.OrderService;
import com.example.virtualwinesommelierbackend.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
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
class UserControllerTest {
    protected static MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private JwtUtil jwtUtil;

    @MockBean
    private OrderService orderService;

    @MockBean
    private UserService userService;

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
    void getUserInfo_ShouldReturnUserProfile() throws Exception {
        Long userId = 1L;
        UserDto expectedUser = new UserDto()
                .setId(1L)
                .setFirstName("John")
                .setLastName("Doe")
                .setEmail("john.doe@example.com")
                .setShippingAddress(new AddressDto()
                        .setArea("Area")
                        .setStreet("123 Main St")
                        .setCity("Springfield")
                        .setZipCode("12345"));

        Mockito.when(authService.getUserId()).thenReturn(userId);
        Mockito.when(userService.getUserInfo(userId)).thenReturn(expectedUser);

        MvcResult result = mockMvc.perform(get("/api/users/profile"))
                .andExpect(status().isOk())
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        UserDto actualUser = objectMapper.readValue(jsonResponse, UserDto.class);

        Assertions.assertNotNull(actualUser);
        Assertions.assertEquals(expectedUser.getId(), actualUser.getId());
        Assertions.assertEquals(expectedUser.getEmail(), actualUser.getEmail());
        Assertions.assertEquals(expectedUser.getLastName(), actualUser.getLastName());
    }

    @Test
    @WithMockUser(roles = "USER")
    void updateUserInfo_ShouldReturnUpdatedUserProfile() throws Exception {
        UserRequestDto requestDto = new UserRequestDto()
                .setFirstName("John")
                .setLastName("Doe")
                .setEmail("john.doe@example.com")
                .setShippingAddress(new AddressRequestDto()
                        .setArea("Area")
                        .setStreet("123 Main St")
                        .setCity("Springfield")
                        .setZipCode("12345"));
        UserDto expectedUser = new UserDto()
                .setId(1L)
                .setFirstName("John")
                .setLastName("Doe")
                .setEmail("john.doe@example.com")
                .setShippingAddress(new AddressDto()
                        .setArea("Area")
                        .setStreet("123 Main St")
                        .setCity("Springfield")
                        .setZipCode("12345"));

        Mockito.when(userService.updateUserInfo(any(UserRequestDto.class)))
                .thenReturn(expectedUser);

        String jsonRequest = objectMapper.writeValueAsString(requestDto);

        MvcResult result = mockMvc.perform(post("/api/users/profile")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isOk())
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        UserDto actualUser = objectMapper.readValue(jsonResponse, UserDto.class);

        Assertions.assertNotNull(actualUser);
        Assertions.assertEquals(expectedUser.getId(), actualUser.getId());
        Assertions.assertEquals(expectedUser.getEmail(), actualUser.getEmail());
        Assertions.assertEquals(expectedUser.getLastName(), actualUser.getLastName());
    }

    @Test
    @WithMockUser(roles = "USER")
    void getAllOrders_ShouldReturnOrderList() throws Exception {
        List<OrderDto> expectedOrders = List.of(
                new OrderDto()
                        .setId(1L)
                        .setUserId(1L)
                        .setOrderDate(LocalDateTime.now())
                        .setTotal(BigDecimal.valueOf(100.00))
                        .setStatus(Order.Status.PROCESSING)
                        .setOrderItems(Set.of(
                                new OrderItemDto().setId(1L).setWineId(1L).setQuantity(2)
                        )),
                new OrderDto()
                        .setId(2L)
                        .setUserId(1L)
                        .setOrderDate(LocalDateTime.now())
                        .setTotal(BigDecimal.valueOf(200.00))
                        .setStatus(Order.Status.COMPLETED)
                        .setOrderItems(Set.of(
                                new OrderItemDto().setId(2L).setWineId(2L).setQuantity(4)
                        ))
        );

        Mockito.when(orderService.findAll()).thenReturn(expectedOrders);

        MvcResult result = mockMvc.perform(get("/api/users/orders"))
                .andExpect(status().isOk())
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        List<OrderDto> actualOrders = objectMapper.readValue(
                jsonResponse,
                objectMapper.getTypeFactory().constructCollectionType(List.class, OrderDto.class)
        );

        Assertions.assertNotNull(actualOrders);
        Assertions.assertEquals(expectedOrders.size(), actualOrders.size());
        Assertions.assertEquals(expectedOrders.get(0).getId(), actualOrders.get(0).getId());
        Assertions.assertEquals(expectedOrders.get(1).getTotal(), actualOrders.get(1).getTotal());
        Assertions.assertEquals(expectedOrders.get(0).getUserId(), actualOrders.get(0).getUserId());
    }
}

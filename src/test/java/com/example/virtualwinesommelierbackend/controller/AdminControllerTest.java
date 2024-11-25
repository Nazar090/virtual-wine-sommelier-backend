package com.example.virtualwinesommelierbackend.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.virtualwinesommelierbackend.dto.address.AddressDto;
import com.example.virtualwinesommelierbackend.dto.order.OrderDto;
import com.example.virtualwinesommelierbackend.dto.order.OrderStatusDto;
import com.example.virtualwinesommelierbackend.dto.orderitem.OrderItemDto;
import com.example.virtualwinesommelierbackend.dto.user.profile.UserDto;
import com.example.virtualwinesommelierbackend.dto.user.profile.UserRoleRequestDto;
import com.example.virtualwinesommelierbackend.dto.wine.WineDto;
import com.example.virtualwinesommelierbackend.dto.wine.WineRequestDto;
import com.example.virtualwinesommelierbackend.model.Order;
import com.example.virtualwinesommelierbackend.security.JwtUtil;
import com.example.virtualwinesommelierbackend.service.OrderService;
import com.example.virtualwinesommelierbackend.service.UserService;
import com.example.virtualwinesommelierbackend.service.WineService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AdminControllerTest {
    protected static MockMvc mockMvc;

    @MockBean
    private JwtUtil jwtUtil;

    @MockBean
    private WineService wineService;

    @MockBean
    private OrderService orderService;

    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeAll
    static void beforeAll(
            @Autowired WebApplicationContext applicationContext
    ) {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(applicationContext)
                .apply(springSecurity())
                .build();
    }

    // Catalog Tests
    @Test
    @AutoConfigureMockMvc
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void addProduct_ShouldReturnCreatedWine() throws Exception {
        WineRequestDto requestDto = new WineRequestDto()
                .setName("Sample Wine")
                .setType("Dry")
                .setColor("Red")
                .setStrength("13%")
                .setCountry("France")
                .setGrape("Merlot")
                .setPrice(BigDecimal.valueOf(20.00))
                .setDescription("Description here");

        WineDto expected = new WineDto(1L, "Sample Wine", "Dry",
                "Red", "13%", "France", "Merlot",
                BigDecimal.valueOf(20.00), "Description here");
        when(wineService.save(any(WineRequestDto.class))).thenReturn(expected);

        String jsonRequest = objectMapper.writeValueAsString(requestDto);
        MvcResult result = mockMvc.perform(post("/api/admin/catalog")
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isCreated())
                .andReturn();

        WineDto actual = objectMapper.readValue(result.getResponse().getContentAsString(),
                WineDto.class);
        Assertions.assertNotNull(actual);
        Assertions.assertNotNull(actual.id());
        Assertions.assertEquals(expected.name(), actual.name());
        Assertions.assertEquals(expected, actual);

    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void updateProduct_ShouldReturnUpdatedWine() throws Exception {
        Long wineId = 1L;
        WineRequestDto updateRequestDto = new WineRequestDto()
                .setName("Updated Wine")
                .setType("Semi-Dry")
                .setColor("White")
                .setStrength("12%")
                .setCountry("Italy")
                .setGrape("Chardonnay")
                .setPrice(BigDecimal.valueOf(25.00))
                .setDescription("Updated description");

        WineDto expected = new WineDto(wineId, "Updated Wine", "Semi-Dry",
                "White", "12%", "Italy", "Chardonnay",
                BigDecimal.valueOf(25.00), "Updated description");

        when(wineService.update(eq(1L), any(WineRequestDto.class))).thenReturn(expected);

        String jsonRequest = objectMapper.writeValueAsString(updateRequestDto);
        MvcResult result = mockMvc.perform(put("/api/admin/catalog/{id}", wineId)
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();

        WineDto actual = objectMapper.readValue(result.getResponse().getContentAsString(),
                WineDto.class);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(expected.id(), actual.id());
        Assertions.assertEquals(expected.name(), actual.name());
        Assertions.assertEquals(expected, actual);
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void deleteProduct_ShouldReturnNoContent() throws Exception {
        mockMvc.perform(delete("/api/admin/catalog/1"))
                .andExpect(status().isNoContent());
    }

    // Order Tests
    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void getAllOrders_ShouldReturnOrderList() throws Exception {
        OrderDto orderDto = new OrderDto()
                .setId(1L)
                .setUserId(1L)
                .setOrderItems(Set.of(new OrderItemDto()))
                .setOrderDate(LocalDateTime.now())
                .setTotal(BigDecimal.valueOf(50.00))
                .setStatus(Order.Status.PROCESSING);
        when(orderService.findAll()).thenReturn(List.of(orderDto));

        MvcResult result = mockMvc.perform(get("/api/admin/orders")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();

        List<OrderDto> actualOrders = Arrays.asList(objectMapper.readValue(jsonResponse,
                OrderDto[].class));
        Assertions.assertNotNull(actualOrders);
        Assertions.assertEquals(1, actualOrders.size());
        Assertions.assertEquals(orderDto.getStatus(), actualOrders.get(0).getStatus());
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void updateOrderStatus_ShouldReturnUpdatedOrder() throws Exception {
        Long orderId = 1L;
        OrderStatusDto statusDto = new OrderStatusDto();
        statusDto.setStatus(Order.Status.PROCESSING);
        OrderDto orderDto = new OrderDto()
                .setId(1L)
                .setUserId(1L)
                .setOrderItems(Set.of(new OrderItemDto()))
                .setOrderDate(LocalDateTime.now())
                .setTotal(BigDecimal.valueOf(50.00))
                .setStatus(Order.Status.PROCESSING);
        when(orderService.updateStatus(eq(orderId), any(OrderStatusDto.class)))
                .thenReturn(orderDto);

        String jsonRequest = objectMapper.writeValueAsString(statusDto);

        MvcResult result = mockMvc.perform(put("/api/admin/orders/{id}/status", orderId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isOk())
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        OrderDto actualOrder = objectMapper.readValue(jsonResponse, OrderDto.class);
        Assertions.assertNotNull(actualOrder);
        Assertions.assertEquals(orderDto.getTotal(), actualOrder.getTotal());
    }

    // User Tests
    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void getAllUsers_ShouldReturnUserList() throws Exception {
        AddressDto addressDto = new AddressDto()
                .setArea("Dublin")
                .setCity("Dublin")
                .setStreet("43 avenue")
                .setZipCode("65890");
        UserDto userDto = new UserDto()
                .setFirstName("John")
                .setLastName("Doe")
                .setEmail("john.doe@example.com")
                .setShippingAddress(addressDto);
        when(userService.getAll()).thenReturn(List.of(userDto));

        MvcResult result = mockMvc.perform(get("/api/admin/users")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        List<UserDto> actualUsers = Arrays.asList(objectMapper.readValue(jsonResponse,
                UserDto[].class));
        Assertions.assertNotNull(actualUsers);
        Assertions.assertEquals(1, actualUsers.size());
        Assertions.assertEquals(userDto.getEmail(), actualUsers.get(0).getEmail());
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void updateDetails_ShouldReturnUpdatedUser() throws Exception {
        Long userId = 1L;
        UserRoleRequestDto requestDto = new UserRoleRequestDto();
        AddressDto addressDto = new AddressDto()
                .setArea("Dublin")
                .setCity("Dublin")
                .setStreet("43 avenue")
                .setZipCode("65890");
        UserDto userDto = new UserDto()
                .setFirstName("John")
                .setLastName("Doe")
                .setEmail("john.doe@example.com")
                .setShippingAddress(addressDto);
        when(userService.updateUserRoleInfo(eq(userId), any(UserRoleRequestDto.class)))
                .thenReturn(userDto);

        String jsonRequest = objectMapper.writeValueAsString(requestDto);

        MvcResult result = mockMvc.perform(put("/api/admin/users/{id}", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isOk())
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        UserDto actualUser = objectMapper.readValue(jsonResponse, UserDto.class);
        Assertions.assertNotNull(actualUser);
        Assertions.assertEquals(userDto.getId(), actualUser.getId());
    }
}

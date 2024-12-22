package com.example.virtualwinesommelierbackend.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.virtualwinesommelierbackend.dto.address.AddressDto;
import com.example.virtualwinesommelierbackend.dto.order.OrderDto;
import com.example.virtualwinesommelierbackend.dto.order.OrderStatusDto;
import com.example.virtualwinesommelierbackend.dto.user.profile.UserDto;
import com.example.virtualwinesommelierbackend.dto.user.profile.UserRoleRequestDto;
import com.example.virtualwinesommelierbackend.dto.wine.WineDto;
import com.example.virtualwinesommelierbackend.dto.wine.WineRequestDto;
import com.example.virtualwinesommelierbackend.model.Order;
import com.example.virtualwinesommelierbackend.model.Role;
import com.example.virtualwinesommelierbackend.security.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@Sql(scripts = {
        "classpath:database/cleanup/cleanup-tables.sql",
        "classpath:database/initialize/add-users-and-roles.sql",
        "classpath:database/initialize/add-user-order-wines-orderitems.sql",
}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AdminControllerTest {
    protected static MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private JwtUtil jwtUtil;

    @BeforeAll
    static void beforeAll(
            @Autowired WebApplicationContext applicationContext
    ) {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(applicationContext)
                .apply(springSecurity())
                .build();
    }

    // Wine Catalog Tests

    @Test
    @WithMockUser(roles = "ADMIN")
    void addWine_ShouldReturnCreatedWine() throws Exception {
        // Given
        WineRequestDto requestDto = createWineRequestDto("Wine Test","Semi-Dry", "Red");
        WineDto expectedWine = createWineDto(1L, "Wine Test","Semi-Dry", "Red");

        // When
        MvcResult result = mockMvc.perform(post("/api/admin/catalog")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated())
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        WineDto actualWine = objectMapper.readValue(jsonResponse, WineDto.class);

        // Then
        assertNotNull(actualWine);
        assertEquals(expectedWine.getName(), actualWine.getName());
        assertEquals(expectedWine.getCountry(), actualWine.getCountry());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void updateProduct_ShouldReturnUpdatedWine() throws Exception {
        // Given
        Long wineId = 1L;
        WineRequestDto requestDto = createWineRequestDto("Wine","Dry", "White");
        WineDto expectedWine = createWineDto(wineId, "Wine","Dry", "White");

        // When
        MvcResult result = mockMvc.perform(put("/api/admin/catalog/{id}", wineId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        WineDto actualWine = objectMapper.readValue(jsonResponse, WineDto.class);

        // Then
        assertNotNull(actualWine);
        assertEquals(expectedWine.getName(), actualWine.getName());
        assertEquals(expectedWine.getCountry(), actualWine.getCountry());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deleteProduct_ShouldReturnNoContent() throws Exception {
        // Given
        Long wineId = 1L;

        // When/Then
        mockMvc.perform(delete("/api/admin/catalog/{id}", wineId))
                .andExpect(status().isNoContent());
    }

    // Order Management Tests

    @Test
    @WithMockUser(roles = "ADMIN")
    void getAllOrders_ShouldReturnOrderList() throws Exception {
        // Given
        List<OrderDto> expectedOrders = List.of(
                createOrderDto(1L, BigDecimal.valueOf(100.05)),
                createOrderDto(2L, BigDecimal.valueOf(40.00))
        );

        // When
        MvcResult result = mockMvc.perform(get("/api/admin/orders"))
                .andExpect(status().isOk())
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        List<OrderDto> actualOrders = objectMapper.readValue(
                jsonResponse,
                objectMapper.getTypeFactory().constructCollectionType(List.class, OrderDto.class)
        );

        // Then
        assertEquals(expectedOrders.size(), actualOrders.size());
        assertEquals(expectedOrders.get(0).getTotal(), actualOrders.get(0).getTotal());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void updateOrderStatus_ShouldReturnUpdatedOrder() throws Exception {
        // Given
        Long orderId = 1L;
        OrderStatusDto statusDto = new OrderStatusDto().setStatus(Order.Status.PROCESSING);

        // When
        MvcResult result = mockMvc.perform(put("/api/admin/orders/{id}/status", orderId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(statusDto)))
                .andExpect(status().isOk())
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        OrderDto actualOrder = objectMapper.readValue(jsonResponse, OrderDto.class);

        // Then
        assertNotNull(actualOrder);
        assertEquals(Order.Status.PROCESSING, actualOrder.getStatus());
    }

    // User Management Tests

    @Test
    @WithMockUser(roles = "ADMIN")
    void getAllUsers_ShouldReturnUserList() throws Exception {
        // Given
        List<UserDto> expectedUsers = List.of(
                createUserDto(1L, "user1@example.com"),
                createUserDto(2L, "user2@example.com"),
                createUserDto(3L, "testuser@example.com")
        );

        // When
        MvcResult result = mockMvc.perform(get("/api/admin/users"))
                .andExpect(status().isOk())
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        List<UserDto> actualUsers = objectMapper.readValue(
                jsonResponse,
                objectMapper.getTypeFactory().constructCollectionType(List.class, UserDto.class)
        );

        // Then
        assertEquals(expectedUsers.size(), actualUsers.size());
        assertEquals(expectedUsers.get(0).getEmail(), actualUsers.get(0).getEmail());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void updateDetails_ShouldReturnUpdatedUser() throws Exception {
        // Given
        Long userId = 1L;
        UserRoleRequestDto requestDto = createUpdatedDto();
        UserDto expectedUser = new UserDto()
                .setId(userId)
                .setFirstName("NameUpdated")
                .setLastName("LastNameUpdated")
                .setEmail("updated@gmail.com");

        // When
        MvcResult result = mockMvc.perform(put("/api/admin/users/{id}", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        UserDto actualUser = objectMapper.readValue(jsonResponse, UserDto.class);

        // Then
        assertNotNull(actualUser);
        assertEquals(expectedUser.getEmail(), actualUser.getEmail());
        assertEquals(expectedUser.getLastName(), actualUser.getLastName());
    }

    // Helper Methods

    private UserRoleRequestDto createUpdatedDto() {
        Role updatedRole = new Role();
        updatedRole.setRole(Role.RoleName.ADMIN);
        return new UserRoleRequestDto()
                .setFirstName("NameUpdated")
                .setLastName("LastNameUpdated")
                .setEmail("updated@gmail.com")
                .setShippingAddress(
                        new AddressDto()
                                .setArea("Area")
                                .setCity("City")
                                .setStreet("Street")
                                .setZipCode("Zip Code"))
                .setRole(updatedRole);
    }

    private WineDto createWineDto(Long id, String name, String type, String color) {
        return new WineDto()
                .setId(id)
                .setName(name)
                .setType(type)
                .setColor(color)
                .setStrength("13%")
                .setCountry("Italy")
                .setGrape("Merlot")
                .setPrice(BigDecimal.valueOf(20.00))
                .setDescription("Rich red wine")
                .setImageUrl("url");
    }

    private WineRequestDto createWineRequestDto(String name, String type, String color) {
        return new WineRequestDto()
                .setName(name)
                .setType(type)
                .setColor(color)
                .setStrength("13%")
                .setCountry("Italy")
                .setGrape("Merlot")
                .setPrice(BigDecimal.valueOf(20.00))
                .setDescription("Rich red wine")
                .setImageUrl("url");
    }

    private OrderDto createOrderDto(Long id, BigDecimal total) {
        return new OrderDto()
                .setId(id)
                .setUserId(1L)
                .setOrderDate(LocalDateTime.now())
                .setTotal(total)
                .setStatus(Order.Status.COMPLETED);
    }

    private UserDto createUserDto(Long id, String email) {
        return new UserDto()
                .setId(id)
                .setFirstName("John")
                .setLastName("Doe")
                .setEmail(email);
    }
}

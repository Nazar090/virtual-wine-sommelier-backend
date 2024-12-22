package com.example.virtualwinesommelierbackend.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.virtualwinesommelierbackend.dto.address.AddressRequestDto;
import com.example.virtualwinesommelierbackend.dto.order.OrderDto;
import com.example.virtualwinesommelierbackend.dto.order.OrderRequestDto;
import com.example.virtualwinesommelierbackend.dto.orderitem.OrderItemDto;
import com.example.virtualwinesommelierbackend.model.Order;
import com.example.virtualwinesommelierbackend.security.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.testcontainers.junit.jupiter.Testcontainers;

@Sql(scripts = {
        "classpath:database/cleanup/cleanup-tables.sql",
        "classpath:database/initialize/add-users-and-roles.sql",
        "classpath:database/initialize/add-user-order-wines-orderitems.sql",
}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
class OrderControllerTest {
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

    @Test
    @WithMockUser(roles = "USER")
    void getOrderItems_ShouldReturnOrderItemsList() throws Exception {
        // Given
        Long orderId = 1L;

        // When
        MvcResult result = mockMvc.perform(get("/api/orders/{orderId}/items", orderId))
                .andExpect(status().isOk())
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        List<OrderItemDto> actualItems = objectMapper.readValue(jsonResponse,
                objectMapper.getTypeFactory().constructCollectionType(List.class,
                        OrderItemDto.class));

        // Then
        assertNotNull(actualItems);
        assertEquals(1, actualItems.size());
        assertEquals(1L, actualItems.get(0).getWineId());
    }

    @Test
    @WithMockUser(roles = "USER")
    void getItemById_ShouldReturnOrderItem() throws Exception {
        // Given
        Long orderId = 2L;
        Long itemId = 2L;

        // When
        MvcResult result = mockMvc.perform(get("/api/orders/{orderId}/items/{itemId}",
                        orderId, itemId))
                .andExpect(status().isOk())
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        OrderItemDto actualItem = objectMapper.readValue(jsonResponse, OrderItemDto.class);

        // Then
        assertNotNull(actualItem);
        assertEquals(2L, actualItem.getWineId());
        assertEquals(3, actualItem.getQuantity());
    }

    // Helper Methods

    private OrderRequestDto createOrderRequestDto() {
        return new OrderRequestDto()
                .setShippingAddress(new AddressRequestDto()
                        .setArea("Area")
                        .setStreet("123 Main St")
                        .setCity("Springfield")
                        .setZipCode("12345678"));
    }

    private OrderDto createOrderDto() {
        return new OrderDto()
                .setId(1L)
                .setUserId(1L)
                .setOrderItems(Set.of(new OrderItemDto(), new OrderItemDto()))
                .setOrderDate(LocalDateTime.now())
                .setTotal(BigDecimal.TEN)
                .setStatus(Order.Status.PROCESSING);
    }
}

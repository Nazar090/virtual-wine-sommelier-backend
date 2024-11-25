package com.example.virtualwinesommelierbackend.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.virtualwinesommelierbackend.dto.address.AddressRequestDto;
import com.example.virtualwinesommelierbackend.dto.order.OrderDto;
import com.example.virtualwinesommelierbackend.dto.order.OrderRequestDto;
import com.example.virtualwinesommelierbackend.dto.orderitem.OrderItemDto;
import com.example.virtualwinesommelierbackend.model.Order;
import com.example.virtualwinesommelierbackend.security.AuthenticationService;
import com.example.virtualwinesommelierbackend.security.JwtUtil;
import com.example.virtualwinesommelierbackend.service.OrderService;
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
class OrderControllerTest {
    protected static MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private JwtUtil jwtUtil;

    @MockBean
    private OrderService orderService;

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
    void createOrder_ShouldReturnCreatedOrder() throws Exception {
        OrderRequestDto requestDto = new OrderRequestDto()
                .setShippingAddress(new AddressRequestDto()
                        .setArea("Area")
                        .setStreet("123 Main St")
                        .setCity("Springfield")
                        .setZipCode("12345"));

        OrderDto expected = new OrderDto()
                .setId(1L)
                .setUserId(1L)
                .setOrderItems(Set.of(new OrderItemDto(), new OrderItemDto()))
                .setOrderDate(LocalDateTime.now())
                .setTotal(BigDecimal.TEN)
                .setStatus(Order.Status.PROCESSING);

        Mockito.when(authService.getUserId()).thenReturn(1L);
        Mockito.when(orderService.createOrder(eq(1L), any(OrderRequestDto.class)))
                .thenReturn(expected);

        String jsonRequest = objectMapper.writeValueAsString(requestDto);

        MvcResult result = mockMvc.perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isCreated())
                .andReturn();

        OrderDto actualResponse = objectMapper.readValue(result.getResponse().getContentAsString(),
                OrderDto.class);

        Assertions.assertNotNull(actualResponse);
        Assertions.assertEquals(expected.getId(), actualResponse.getId());
        Assertions.assertEquals(expected.getOrderItems().size(), actualResponse.getOrderItems()
                .size());
        Assertions.assertEquals(expected.getUserId(), actualResponse.getUserId());
    }

    @Test
    @WithMockUser(roles = "USER")
    void getOrderItems_ShouldReturnOrderItemsList() throws Exception {
        Long orderId = 1L;
        List<OrderItemDto> expectedItems = List.of(
                new OrderItemDto()
                        .setId(1L)
                        .setQuantity(3)
                        .setWineId(1L),
                new OrderItemDto()
                        .setId(2L)
                        .setQuantity(4)
                        .setWineId(2L)
        );

        Mockito.when(orderService.getOrderItemsByOrderId(eq(orderId))).thenReturn(expectedItems);

        MvcResult result = mockMvc.perform(get("/api/orders/{orderId}/items", orderId))
                .andExpect(status().isOk())
                .andReturn();

        List<OrderItemDto> actualItems = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                objectMapper.getTypeFactory().constructCollectionType(List.class,
                        OrderItemDto.class)
        );

        Assertions.assertNotNull(actualItems);
        Assertions.assertEquals(expectedItems.size(), actualItems.size());
        Assertions.assertEquals(expectedItems.get(0).getId(), actualItems.get(0).getId());
    }

    @Test
    @WithMockUser(roles = "USER")
    void getItemById_ShouldReturnOrderItem() throws Exception {
        Long orderId = 1L;
        Long itemId = 2L;

        OrderItemDto expectedItem = new OrderItemDto()
                .setId(itemId)
                .setWineId(1L)
                .setQuantity(3);

        Mockito.when(orderService.getItemById(eq(orderId), eq(itemId))).thenReturn(expectedItem);

        MvcResult result = mockMvc.perform(get("/api/orders/{orderId}/items/{itemId}",
                        orderId, itemId))
                .andExpect(status().isOk())
                .andReturn();

        OrderItemDto actualItem = objectMapper.readValue(result.getResponse().getContentAsString(),
                OrderItemDto.class);

        Assertions.assertNotNull(actualItem);
        Assertions.assertEquals(expectedItem.getId(), actualItem.getId());
        Assertions.assertEquals(expectedItem.getWineId(), actualItem.getWineId());
        Assertions.assertEquals(expectedItem.getQuantity(), actualItem.getQuantity());
    }
}

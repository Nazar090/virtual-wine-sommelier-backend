package com.example.virtualwinesommelierbackend.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.virtualwinesommelierbackend.dto.wine.WineDto;
import com.example.virtualwinesommelierbackend.security.JwtUtil;
import com.example.virtualwinesommelierbackend.service.WineService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Pageable;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ProductControllerTest {
    protected static MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private JwtUtil jwtUtil;

    @MockBean
    private WineService wineService;

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
    void findById_ShouldReturnWine() throws Exception {
        Long wineId = 1L;
        WineDto expectedWine = new WineDto(
                1L, "Sample Wine", "Red", "France",
                "Merlot", "Dry", "13%",
                BigDecimal.valueOf(20.00), "Description here"
        );

        Mockito.when(wineService.getById(eq(wineId))).thenReturn(expectedWine);

        MvcResult result = mockMvc.perform(get("/api/products/{id}", wineId))
                .andExpect(status().isOk())
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        WineDto actualWine = objectMapper.readValue(jsonResponse, WineDto.class);

        Assertions.assertNotNull(actualWine);
        Assertions.assertEquals(expectedWine.id(), actualWine.id());
        Assertions.assertEquals(expectedWine.name(), actualWine.name());
        Assertions.assertEquals(expectedWine, actualWine);
    }

    @Test
    @WithMockUser(roles = "USER")
    void findAll_ShouldReturnPaginatedWineList() throws Exception {
        List<WineDto> expectedWines = List.of(
                new WineDto(1L, "Wine 1", "Red", "France",
                        "Merlot", "Dry", "13%",
                        BigDecimal.valueOf(25.00), "Wine 1 description"),

                new WineDto(2L, "Wine 2", "White", "Italy",
                        "Chardonnay", "Semi-Dry", "12%",
                        BigDecimal.valueOf(30.00), "Wine 2 description")
        );

        Mockito.when(wineService.getAll(any(Pageable.class))).thenReturn(expectedWines);

        MvcResult result = mockMvc.perform(get("/api/products")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        List<WineDto> actualWines = objectMapper.readValue(
                jsonResponse,
                objectMapper.getTypeFactory().constructCollectionType(List.class, WineDto.class)
        );

        Assertions.assertNotNull(actualWines);
        Assertions.assertEquals(expectedWines.size(), actualWines.size());
        Assertions.assertEquals(expectedWines, actualWines);
    }
}

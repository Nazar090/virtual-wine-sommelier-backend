package com.example.virtualwinesommelierbackend.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.virtualwinesommelierbackend.dto.wine.ProductDto;
import com.example.virtualwinesommelierbackend.dto.wine.WineDto;
import com.example.virtualwinesommelierbackend.security.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.util.List;
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

@Sql(scripts = {
        "classpath:database/cleanup/cleanup-tables.sql",
        "classpath:database/initialize/add-users-and-roles.sql",
        "classpath:database/initialize/add-user-order-wines-orderitems.sql",
}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ProductControllerTest {
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
    void findById_ShouldReturnWine() throws Exception {
        Long wineId = 1L;
        WineDto expectedWine = createWineDto(1L, "Wine 1", "Dry");

        MvcResult result = mockMvc.perform(get("/api/products/{id}", wineId))
                .andExpect(status().isOk())
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        WineDto actualWine = objectMapper.readValue(jsonResponse, WineDto.class);

        assertNotNull(actualWine);
        assertEquals(expectedWine.getId(), actualWine.getId());
        assertEquals(expectedWine.getName(), actualWine.getName());
    }

    @Test
    @WithMockUser(roles = "USER")
    void findAll_ShouldReturnPaginatedWineList() throws Exception {
        List<WineDto> wines = List.of(
                createWineDto(1L, "Wine 1", "Dry"),
                createWineDto(2L, "Wine 2", "Semi-Dry"));
        ProductDto expectedWines = new ProductDto();
        expectedWines.setWineDtos(wines);
        expectedWines.setTotalProducts(wines.size());

        MvcResult result = mockMvc.perform(get("/api/products"))
                .andExpect(status().isOk())
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        ProductDto actualWines = objectMapper.readValue(jsonResponse, ProductDto.class);

        assertNotNull(actualWines);
        assertEquals(expectedWines.getWineDtos().size(),
                actualWines.getWineDtos().size());
    }

    private WineDto createWineDto(Long id, String name, String type) {
        return new WineDto()
                .setId(id)
                .setName(name)
                .setType(type)
                .setColor("Red")
                .setStrength("13%")
                .setCountry("France")
                .setPrice(BigDecimal.valueOf(20.00))
                .setDescription("Description here");
    }
}

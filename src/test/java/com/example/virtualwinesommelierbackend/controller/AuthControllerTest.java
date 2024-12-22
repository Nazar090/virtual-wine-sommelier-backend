package com.example.virtualwinesommelierbackend.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.virtualwinesommelierbackend.dto.address.AddressRequestDto;
import com.example.virtualwinesommelierbackend.dto.user.UserLoginDto;
import com.example.virtualwinesommelierbackend.dto.user.UserLoginRequestDto;
import com.example.virtualwinesommelierbackend.dto.user.UserRegistrationDto;
import com.example.virtualwinesommelierbackend.dto.user.UserRegistrationRequestDto;
import com.example.virtualwinesommelierbackend.exception.RegistrationException;
import com.example.virtualwinesommelierbackend.security.AuthenticationService;
import com.example.virtualwinesommelierbackend.security.JwtUtil;
import com.example.virtualwinesommelierbackend.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AuthControllerTest {
    protected static MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private JwtUtil jwtUtil;

    @MockBean
    private UserService userService;

    @MockBean
    private AuthenticationService authenticationService;

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
    void registerUser_ShouldReturnRegisteredUser() throws Exception {
        UserRegistrationRequestDto requestDto = createUserRegistration();
        UserRegistrationDto expected = new UserRegistrationDto()
                .setId(1L)
                .setEmail("john.doe@example.com");

        Mockito.when(userService.register(any(UserRegistrationRequestDto.class)))
                .thenReturn(expected);

        String jsonRequest = objectMapper.writeValueAsString(requestDto);

        MvcResult result = mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isOk())
                .andReturn();

        UserRegistrationDto actual = objectMapper.readValue(result.getResponse()
                .getContentAsString(), UserRegistrationDto.class);
        Assertions.assertNotNull(actual);
        Assertions.assertNotNull(actual.getId());
        Assertions.assertEquals(expected.getEmail(), actual.getEmail());
    }

    @Test
    void registerUser_ShouldReturnBadRequestOnRegistrationException() throws Exception {
        UserRegistrationRequestDto requestDto = createUserRegistration();

        Mockito.when(userService.register(any(UserRegistrationRequestDto.class)))
                .thenThrow(new RegistrationException("User already exists"));

        String jsonRequest = objectMapper.writeValueAsString(requestDto);

        MvcResult result = mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isConflict())
                .andReturn();

        String errorMessage = result.getResponse().getContentAsString();
        Assertions.assertEquals("User already exists", errorMessage);
    }

    @Test
    void login_ShouldReturnJwtToken() throws Exception {
        UserLoginRequestDto requestDto = new UserLoginRequestDto()
                .setEmail("john.doe@example.com")
                .setPassword("password123");
        UserLoginDto expectedResponse = new UserLoginDto().setToken("some-jwt-token");

        Mockito.when(authenticationService.authenticate(any(UserLoginRequestDto.class)))
                .thenReturn(expectedResponse);

        String jsonRequest = objectMapper.writeValueAsString(requestDto);

        MvcResult result = mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isOk())
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        UserLoginDto actualResponse = objectMapper.readValue(jsonResponse, UserLoginDto.class);

        Assertions.assertNotNull(actualResponse);
        Assertions.assertEquals(expectedResponse.getToken(), actualResponse.getToken());
    }

    @Test
    void logout_ShouldInvalidateSession() throws Exception {
        MvcResult result = mockMvc.perform(post("/api/auth/logout"))
                .andExpect(status().isOk())
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        Assertions.assertEquals("{\"message\": \"Logout successful\"}", jsonResponse);
    }

    // Helper Methods

    private UserRegistrationRequestDto createUserRegistration() {
        return new UserRegistrationRequestDto()
                .setFirstName("John")
                .setLastName("Doe")
                .setEmail("john.doe@example.com")
                .setPassword("Password@123")
                .setRepeatPassword("Password@123")
                .setShippingAddress(new AddressRequestDto()
                        .setArea("Area")
                        .setStreet("123 Main St")
                        .setCity("Springfield")
                        .setZipCode("12345"));
    }
}

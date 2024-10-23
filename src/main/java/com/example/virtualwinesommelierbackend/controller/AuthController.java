package com.example.virtualwinesommelierbackend.controller;

import com.example.virtualwinesommelierbackend.dto.user.UserLoginDto;
import com.example.virtualwinesommelierbackend.dto.user.UserLoginRequestDto;
import com.example.virtualwinesommelierbackend.dto.user.UserRegistrationDto;
import com.example.virtualwinesommelierbackend.dto.user.UserRegistrationRequestDto;
import com.example.virtualwinesommelierbackend.exception.RegistrationException;
import com.example.virtualwinesommelierbackend.security.AuthenticationService;
import com.example.virtualwinesommelierbackend.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller responsible for handling user authentication-related actions such as
 * registration, login, and logout. It uses UserService for user-related actions and
 * AuthenticationService for authentication logic.
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth")
@Tag(name = "Authentication", description = "User authentications")
public class AuthController {
    private final UserService userService;
    private final AuthenticationService authenticationService;

    /**
     * Registers a new user account.
     * @param requestDto The user registration request data.
     * @return The registered user's details.
     * @throws RegistrationException if the registration fails.
     */
    @PostMapping("/register")
    @Operation(summary = "User registration",
            description = "User registration in a new account")
    public UserRegistrationDto registerUser(
            @RequestBody @Valid UserRegistrationRequestDto requestDto)
            throws RegistrationException {
        return userService.register(requestDto);
    }

    /**
     * Login to the user account.
     * @param request The user login request data.
     * @return JWT token upon successful login.
     */
    @PostMapping("/login")
    @Operation(summary = "User login", description = "User login to the account")
    public UserLoginDto login(@RequestBody @Valid UserLoginRequestDto request) {
        return authenticationService.authenticate(request);
    }

    /**
     * Logs the user out by invalidating the session
     * @param request The HTTP request, used to invalidate the session.
     * @param response The HTTP response, used to send the logout success message.
     * @throws IOException if an input/output error occurs during logout.
     */
    @PostMapping("/logout")
    @Operation(summary = "User logout", description = "User logout from the account")
    public void logout(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        request.getSession().invalidate();
        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().write("{\"message\": \"Logout successful\"}");
    }
}

package com.example.virtualwinesommelierbackend.security;

import com.example.virtualwinesommelierbackend.dto.user.UserLoginDto;
import com.example.virtualwinesommelierbackend.dto.user.UserLoginRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

/**
 * Service responsible for authentication users and generating JWT tokens.
 */
@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    /**
     * Authenticates the user and generates a JWT token upon successful authentication.
     * @param requestDto Login request for the user to log in to the account.
     * @return Generated JWT token for the authenticated user
     */
    public UserLoginDto authenticate(UserLoginRequestDto requestDto) {
        final Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(requestDto.email(), requestDto.password())
        );
        String token = jwtUtil.generateToken(authentication.getName());

        // Return the token wrapped in UserLoginDto
        return new UserLoginDto(token);
    }
}

package com.example.virtualwinesommelierbackend.security;

import com.example.virtualwinesommelierbackend.dto.user.UserLoginDto;
import com.example.virtualwinesommelierbackend.dto.user.UserLoginRequestDto;
import com.example.virtualwinesommelierbackend.exception.EntityNotFoundException;
import com.example.virtualwinesommelierbackend.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
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

    /**
     * The method responsible for getting the ID of currently authorised user
     *
     * @return user's ID
     */
    public Long getUserId() {
        // Authentication contains details about the authenticated user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            // This cast allows access to user-specific fields
            User user = (User) authentication.getPrincipal();
            return user.getId();
        }
        throw new EntityNotFoundException(
                "Can't find authentication name by authentication " + authentication);
    }
}

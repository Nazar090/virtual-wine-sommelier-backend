package com.example.virtualwinesommelierbackend.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * JWT Authentication Filter to validate the JWT token in the Authorization header.
 * This filter is executed once per request and authenticates the user if the JWT is valid.
 */
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private static final String TOKEN_HEADER = "Bearer ";
    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;

    /**
     * Filters incoming HTTP requests to validate JWT tokens. If a valid token is found,
     * it sets the authentication
     * context in {@link SecurityContextHolder}.
     *
     * @param request  The incoming HTTP request.
     * @param response The outgoing HTTP response.
     * @param filterChain The filter chain that this filter is a part of.
     * @throws ServletException If an error occurs during filtering.
     * @throws IOException      If an I/O error occurs during filtering.
     */
    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        String token = getToken(request);

        if (token != null && jwtUtil.isValidToken(token)) {
            String username = jwtUtil.getUsername(token);
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            Authentication authentication = new UsernamePasswordAuthenticationToken(
                    userDetails, null, userDetails.getAuthorities()
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        filterChain.doFilter(request, response);
    }

    /**
     * Retrieves the JWT token from the Authorization header in the request.
     *
     * @param request The incoming HTTP request.
     * @return The JWT token as a String if present and valid, or null if not found.
     */
    private String getToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(TOKEN_HEADER)) {
            return bearerToken.substring(TOKEN_HEADER.length());
        }
        return null;
    }
}

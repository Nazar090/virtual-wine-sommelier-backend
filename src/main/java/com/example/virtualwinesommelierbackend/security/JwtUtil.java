package com.example.virtualwinesommelierbackend.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.function.Function;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Utility class for handling JWT operations, such as token generation, validation,
 * and extracting claims.
 */
@Component
public class JwtUtil {
    private final Key secret; // Key used to sign and validate JWT tokens
    @Value("${jwt.expiration}")
    private Long expiration; // Expiration time for the JWT token in milliseconds

    public JwtUtil(@Value("${jwt.secret}") String secretString) {
        secret = Keys.hmacShaKeyFor(secretString.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * Generates a JWT token for the given username, with a specified expiration time.
     *
     * @param username The username for which the token is being generated.
     * @return The generated JWT token as a String.
     */
    public String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(secret)
                .compact();
    }

    /**
     * Validates the provided JWT token.
     *
     * @param token The JWT token to validate.
     * @return true if the token is valid and not expired, false otherwise.
     * @throws JwtException if the token is expired or invalid.
     */
    public boolean isValidToken(String token) {
        try {
            Jws<Claims> claimsJws = Jwts.parserBuilder()
                    .setSigningKey(secret)
                    .build()
                    .parseClaimsJws(token);
            return !claimsJws.getBody().getExpiration().before(new Date());
        } catch (JwtException | IllegalArgumentException e) {
            throw new JwtException("Expired or invalid JWT token");
        }
    }

    public String getUsername(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    /**
     * Extracts a specific claim from the JWT token using a claims resolver function.
     *
     * @param token The JWT token.
     * @param claimsResolver A function to extract a claim from the token.
     * @param <T> The type of the claim.
     * @return The extracted claim.
     */
    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = Jwts.parserBuilder()
                .setSigningKey(secret)
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claimsResolver.apply(claims);
    }
}

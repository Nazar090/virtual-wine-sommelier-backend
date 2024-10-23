package com.example.virtualwinesommelierbackend.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class for setting up OpenAPI (Swagger) documentation with JWT-based security.
 * <p>
 *     This configuration adds a security scheme for Bearer tokens (JWT) which is used to
 *     secure API endpoints.
 *     It ensures that the generated API documentation includes security definitions and
 *     requirements.
 * </p>
 */
@Configuration
public class OpenApiConfig {
    private static final String bearerAuth = "BearerAuth";
    private static final String bearer = "bearer";
    private static final String jwt = "JWT";

    /**
     * Bean definition for customizing the OpenAPI specification.
     * <p>
     *     This method sets up the security scheme using Bearer tokens with JWT format.
     *     It adds a security scheme
     *     to the OpenAPI components and applies the security requirement globally to all endpoints.
     * </p>
     *
     * @return a configured instance of {@link OpenAPI}
     */
    @Bean
    public OpenAPI customOpenApi() {
        return new OpenAPI()
                .components(new Components().addSecuritySchemes(bearerAuth,
                        new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP)
                                .scheme(bearer)
                                .bearerFormat(jwt)))
                .addSecurityItem(new SecurityRequirement().addList(bearerAuth));
    }
}

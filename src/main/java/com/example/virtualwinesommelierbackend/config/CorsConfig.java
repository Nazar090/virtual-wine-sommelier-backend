package com.example.virtualwinesommelierbackend.config;

import java.util.Arrays;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 * Global CORS Configuration to enable cross-origin resource sharing.
 * This configuration ensures the backend can communicate with the frontend
 * hosted on different origins during development and production.
 */
@Configuration
public class CorsConfig {
    /**
     * Configures a CORS filter to allow cross-origin requests globally.
     * - Allows requests from specific origins (e.g., localhost for frontend).
     * - Supports commonly used HTTP methods like GET, POST, PUT, DELETE, and OPTIONS.
     * - Permits necessary headers, including Content-Type and Authorization.
     * - Enables credentials support for secure cookies or tokens.
     * - Applies the configuration to all application endpoints.
     * - Sets the filter's order to the highest precedence for priority processing.
     *
     * @return a FilterRegistrationBean configured with the CORS filter.
     */
    @Bean
    public FilterRegistrationBean<CorsFilter> corsFilter() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(Arrays.asList("http://localhost:3000", "https://localhost:3000"));
        config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(Arrays.asList("Content-Type", "Authorization"));
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        FilterRegistrationBean<CorsFilter> bean = new FilterRegistrationBean<>(new CorsFilter(source));
        bean.setOrder(Ordered.HIGHEST_PRECEDENCE);
        return bean;
    }
}

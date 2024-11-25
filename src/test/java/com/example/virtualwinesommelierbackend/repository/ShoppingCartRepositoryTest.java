package com.example.virtualwinesommelierbackend.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.example.virtualwinesommelierbackend.config.CustomPostgresContainer;
import com.example.virtualwinesommelierbackend.model.ShoppingCart;
import com.example.virtualwinesommelierbackend.model.User;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.junit.jupiter.Testcontainers;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Testcontainers
class ShoppingCartRepositoryTest {
    private static final CustomPostgresContainer postgresContainer =
            CustomPostgresContainer.getInstance();

    @Autowired
    private ShoppingCartRepository shoppingCartRepository;

    @Autowired
    private UserRepository userRepository;

    private User user;
    private ShoppingCart shoppingCart;

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgresContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgresContainer::getUsername);
        registry.add("spring.datasource.password", postgresContainer::getPassword);
    }

    @BeforeEach
    void setUp() {
        user = userRepository.findById(1L).orElseThrow(() ->
                new IllegalStateException("User not found"));
        shoppingCart = shoppingCartRepository.findByUserId(user.getId()).orElseThrow(() ->
                new IllegalStateException("ShoppingCart not found"));
    }

    @Test
    @Sql(scripts = "classpath:test-sql/initialize_shopping_cart.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:test-sql/cleanup_shopping_cart.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void findByUserId_ShouldReturnShoppingCartWithCartItems() {
        Optional<ShoppingCart> foundShoppingCart = shoppingCartRepository
                .findByUserId(user.getId());

        assertTrue(foundShoppingCart.isPresent(), "Shopping cart should be found");
        assertEquals(shoppingCart.getId(), foundShoppingCart.get().getId(),
                "Found shopping cart ID should match");
        assertFalse(foundShoppingCart.get().getCartItems().isEmpty(),
                "Shopping cart should contain cart items");
        assertNotNull(foundShoppingCart.get().getCartItems().iterator().next().getWine(),
                "Each cart item should have an associated wine");
    }
}

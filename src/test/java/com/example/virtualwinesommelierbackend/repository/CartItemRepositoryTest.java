package com.example.virtualwinesommelierbackend.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.example.virtualwinesommelierbackend.config.CustomPostgresContainer;
import com.example.virtualwinesommelierbackend.model.CartItem;
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

@Sql(scripts = {
        "classpath:database/cleanup/cleanup-tables.sql",
        "classpath:database/initialize/initialize_cart_items.sql"},
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Testcontainers
class CartItemRepositoryTest {
    private static final CustomPostgresContainer postgresContainer
            = CustomPostgresContainer.getInstance();

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private ShoppingCartRepository shoppingCartRepository;

    @Autowired
    private UserRepository userRepository;

    private ShoppingCart shoppingCart;
    private CartItem cartItem;

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgresContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgresContainer::getUsername);
        registry.add("spring.datasource.password", postgresContainer::getPassword);
    }

    @BeforeEach
    void setUp() {
        User user = userRepository.findById(1L).orElseThrow();
        shoppingCart = shoppingCartRepository.findByUserId(user.getId()).orElseThrow();
        cartItem = cartItemRepository.findById(1L).orElseThrow();
    }

    @Test
    void findByIdAndShoppingCartId_ShouldReturnCartItem() {
        Optional<CartItem> foundCartItem = cartItemRepository
                .findByIdAndShoppingCartId(cartItem.getId(), shoppingCart.getId());

        assertTrue(foundCartItem.isPresent(), "Cart item should be found");
        assertEquals(cartItem.getId(), foundCartItem.get().getId(),
                "Found cart item ID should match");
        assertEquals(shoppingCart.getId(), foundCartItem.get().getShoppingCart().getId(),
                "Shopping cart ID should match");
    }
}

package com.example.virtualwinesommelierbackend.repository;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.example.virtualwinesommelierbackend.config.CustomPostgresContainer;
import com.example.virtualwinesommelierbackend.model.User;
import java.util.Optional;
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
class UserRepositoryTest {
    private static final CustomPostgresContainer postgresContainer =
            CustomPostgresContainer.getInstance();

    @Autowired
    private UserRepository userRepository;

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgresContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgresContainer::getUsername);
        registry.add("spring.datasource.password", postgresContainer::getPassword);
    }

    @Test
    @Sql(scripts = "classpath:test-sql/initialize_user.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:test-sql/cleanup_user.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void existsByEmail_ShouldReturnTrue_WhenUserWithEmailExists() {
        boolean exists = userRepository.existsByEmail("user@example.com");

        assertTrue(exists, "User with specified email should exist");
    }

    @Test
    @Sql(scripts = "classpath:test-sql/initialize_user.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:test-sql/cleanup_user.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void findByEmail_ShouldReturnUserWithRoles_WhenUserWithEmailExists() {
        Optional<User> foundUser = userRepository.findByEmail("user@example.com");

        assertTrue(foundUser.isPresent(),
                "User with specified email should be found");
        assertFalse(foundUser.get().getRoles().isEmpty(),
                "User should have roles eagerly loaded");
    }
}

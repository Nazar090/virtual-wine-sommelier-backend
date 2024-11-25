package com.example.virtualwinesommelierbackend.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.example.virtualwinesommelierbackend.config.CustomPostgresContainer;
import com.example.virtualwinesommelierbackend.model.Role;
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
class RoleRepositoryTest {
    private static final CustomPostgresContainer postgresContainer
            = CustomPostgresContainer.getInstance();
    @Autowired
    private RoleRepository roleRepository;
    private Role role;

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgresContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgresContainer::getUsername);
        registry.add("spring.datasource.password", postgresContainer::getPassword);
    }

    @BeforeEach
    void setUp() {
        role = roleRepository.findById(1L).orElseThrow(() ->
                new IllegalStateException("Role not found"));
    }

    @Test
    @Sql(scripts = "classpath:test-sql/initialize_roles.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:test-sql/cleanup_roles.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void findByRole_ShouldReturnRole_WhenRoleExists() {
        Optional<Role> foundRole = roleRepository.findByRole(Role.RoleName.ADMIN);

        assertTrue(foundRole.isPresent(), "Role should be found");
        assertEquals(role.getId(), foundRole.get().getId(),
                "Found role ID should match");
        assertEquals(Role.RoleName.ADMIN, foundRole.get().getRole(),
                "Role name should match");
    }
}

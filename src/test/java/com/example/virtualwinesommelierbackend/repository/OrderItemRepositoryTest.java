package com.example.virtualwinesommelierbackend.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.example.virtualwinesommelierbackend.config.CustomPostgresContainer;
import com.example.virtualwinesommelierbackend.model.Order;
import com.example.virtualwinesommelierbackend.model.OrderItem;
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
        "classpath:database/initialize/add-user-order-wines-orderitems.sql"},
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Testcontainers
class OrderItemRepositoryTest {
    private static final CustomPostgresContainer postgresContainer =
            CustomPostgresContainer.getInstance();

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private OrderRepository orderRepository;
    private Order order;
    private OrderItem orderItem;

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgresContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgresContainer::getUsername);
        registry.add("spring.datasource.password", postgresContainer::getPassword);
    }

    @BeforeEach
    void setUp() {
        order = orderRepository.findById(1L).orElseThrow();
        orderItem = orderItemRepository.findById(1L).orElseThrow();
    }

    @Test
    void findByIdAndOrderId_ShouldReturnOrderItem() {
        Optional<OrderItem> foundOrderItem = orderItemRepository
                .findByIdAndOrderId(orderItem.getId(), order.getId());

        assertTrue(foundOrderItem.isPresent(), "Order item should be found");
        assertEquals(orderItem.getId(), foundOrderItem.get().getId(),
                "Found order item ID should match");
        assertEquals(order.getId(), foundOrderItem.get().getOrder().getId(),
                "Order ID should match");
    }
}

package com.example.virtualwinesommelierbackend.repository;

import com.example.virtualwinesommelierbackend.model.OrderItem;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    /**
     * Finds an OrderItem by its ID and the associated Order ID.
     *
     * @param orderItemId the ID of the OrderItem
     * @param orderId the ID of the associated Order
     * @return an Optional containing the OrderItem if found, or empty if not found
     */
    Optional<OrderItem> findByIdAndOrderId(Long orderItemId, Long orderId);
}

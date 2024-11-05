package com.example.virtualwinesommelierbackend.repository;

import com.example.virtualwinesommelierbackend.model.OrderItem;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    Optional<OrderItem> findByIdAndOrderId(Long orderItemId, Long orderId);
}

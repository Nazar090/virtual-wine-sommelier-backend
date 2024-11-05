package com.example.virtualwinesommelierbackend.repository;

import com.example.virtualwinesommelierbackend.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}

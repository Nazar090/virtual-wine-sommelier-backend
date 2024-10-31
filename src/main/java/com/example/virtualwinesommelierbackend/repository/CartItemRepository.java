package com.example.virtualwinesommelierbackend.repository;

import com.example.virtualwinesommelierbackend.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
}

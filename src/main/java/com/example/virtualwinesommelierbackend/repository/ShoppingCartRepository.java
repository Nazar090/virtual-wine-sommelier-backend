package com.example.virtualwinesommelierbackend.repository;

import com.example.virtualwinesommelierbackend.model.ShoppingCart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShoppingCartRepository extends JpaRepository<ShoppingCart, Long> {
}

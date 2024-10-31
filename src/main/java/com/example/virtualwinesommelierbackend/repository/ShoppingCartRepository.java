package com.example.virtualwinesommelierbackend.repository;

import com.example.virtualwinesommelierbackend.model.ShoppingCart;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShoppingCartRepository extends JpaRepository<ShoppingCart, Long> {
    /**
     * Retrieves a ShoppingCart for a specific user based on their userId.
     * Utilizes an EntityGraph to eagerly fetch associated cartItems and their wines to optimize
     * query performance and avoid lazy-loading issues in scenarios where these related entities
     * are frequently accessed together.
     *
     * @param userId the ID of the user whose shopping cart is to be fetched
     * @return an Optional containing the ShoppingCart if found, otherwise an empty Optional
     */
    @EntityGraph(attributePaths = {"cartItems", "cartItems.wine"})
    Optional<ShoppingCart> findByUserId(Long userId);
}

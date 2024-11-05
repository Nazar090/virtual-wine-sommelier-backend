package com.example.virtualwinesommelierbackend.repository;

import com.example.virtualwinesommelierbackend.model.CartItem;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    Optional<CartItem> findByIdAndShoppingCartId(Long cartItemId, Long shoppingCartId);

    @Modifying
    @Query("DELETE FROM CartItem ci WHERE ci.shoppingCart.id = :cartId")
    void deleteAllByShoppingCartId(@Param("cartId") Long cartId);
}

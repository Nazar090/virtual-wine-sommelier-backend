package com.example.virtualwinesommelierbackend.repository;

import com.example.virtualwinesommelierbackend.model.CartItem;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    /**
     * Finds a CartItem by its ID and the associated ShoppingCart ID.
     *
     * @param cartItemId the ID of the CartItem
     * @param shoppingCartId the ID of the associated ShoppingCart
     * @return an Optional containing the CartItem if found, or empty if not found
     */
    Optional<CartItem> findByIdAndShoppingCartId(Long cartItemId, Long shoppingCartId);

    /**
     * Deletes all CartItems associated with a specific ShoppingCart ID.
     *
     * @param cartId the ID of the ShoppingCart whose items should be deleted
     */
    @Modifying
    @Query("DELETE FROM CartItem ci WHERE ci.shoppingCart.id = :cartId")
    void deleteAllByShoppingCartId(@Param("cartId") Long cartId);
}

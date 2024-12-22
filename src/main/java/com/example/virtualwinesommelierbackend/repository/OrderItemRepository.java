package com.example.virtualwinesommelierbackend.repository;

import com.example.virtualwinesommelierbackend.model.OrderItem;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    /**
     * Finds an OrderItem by its ID and the associated Order ID.
     *
     * @param orderItemId the ID of the OrderItem
     * @param orderId the ID of the associated Order
     * @return an Optional containing the OrderItem if found, or empty if not found
     */
    Optional<OrderItem> findByIdAndOrderId(Long orderItemId, Long orderId);

    /**
     * Deletes all order items associated with a specific wine.
     * This method is used to manually remove any dependencies
     * in the `order_items` table before deleting a wine from the `wines` table.
     *
     * @param wineId the ID of the wine whose associated order items should be deleted
     */
    @Modifying
    @Query("DELETE FROM OrderItem oi WHERE oi.wine.id = :wineId")
    void deleteByWineId(@Param("wineId") Long wineId);
}

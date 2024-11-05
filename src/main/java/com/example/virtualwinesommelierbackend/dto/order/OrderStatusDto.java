package com.example.virtualwinesommelierbackend.dto.order;

import com.example.virtualwinesommelierbackend.model.Order;
import com.example.virtualwinesommelierbackend.validation.ValidStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderStatusDto {
    @NotNull
    @ValidStatus(message = "Invalid order status. "
            + "Valid values are: PROCESSING, DELIVERED, COMPLETED")
    private Order.Status status;
}

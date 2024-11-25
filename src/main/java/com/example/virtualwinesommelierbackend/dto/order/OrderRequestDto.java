package com.example.virtualwinesommelierbackend.dto.order;

import com.example.virtualwinesommelierbackend.dto.address.AddressRequestDto;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class OrderRequestDto {
    @NotNull(message = "Address can't be null")
    private AddressRequestDto shippingAddress;
}

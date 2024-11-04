package com.example.virtualwinesommelierbackend.validation;

import com.example.virtualwinesommelierbackend.model.Order;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class StatusValidator implements ConstraintValidator<ValidStatus, Order.Status> {
    @Override
    public boolean isValid(Order.Status status, ConstraintValidatorContext context) {
        try {
            Order.Status.valueOf(String.valueOf(status));
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}

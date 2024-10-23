package com.example.virtualwinesommelierbackend.config;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.NullValueCheckStrategy;

/**
 * Global configuration for MapStruct mappers.
 * <p>
 *     This configuration ensures that:
 *     - All mappers are registered as Spring beans using the constructor-based injection.
 *     - Null value checks are performed before mapping.
 *     - Mapper implementations are automatically generated with the class name
 *     followed by prefix 'Impl'.
 * </p>
 */
@org.mapstruct.MapperConfig(
        componentModel = "spring",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
        implementationName = "<CLASS_NAME>Impl"
)
public class MapperConfig {
}

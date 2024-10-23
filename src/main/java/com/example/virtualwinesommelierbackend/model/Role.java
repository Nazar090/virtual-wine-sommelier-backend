package com.example.virtualwinesommelierbackend.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.springframework.security.core.GrantedAuthority;

/**
 * Role model for define User role
 * <p>
 *     Role abilities:
 *     • USER - product client, without management abilities.
 *     • MODER - Moderator role with some management capabilities.
 *     • ADMIN - Full access and management capabilities for the system.
 * </p>
 */
@Entity
@Table(name = "roles")
public class Role implements GrantedAuthority {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true)
    @Enumerated(EnumType.STRING)
    private RoleName role;

    @Override
    public String getAuthority() {
        return role.name();
    }

    public enum RoleName {
        USER,
        MODER,
        ADMIN
    }
}

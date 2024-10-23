package com.example.virtualwinesommelierbackend.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

/**
 * Address model for User shipping data
 */
@Entity
@Getter
@Setter
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank
    @Length(max = 100)
    private String street;
    @NotBlank
    @Length(max = 50)
    private String city;
    @NotBlank
    @Length(max = 50)
    private String area;
    @NotBlank
    @Length(min = 5, max = 10)
    private String zipCode;
    @OneToOne(mappedBy = "shippingAddress", cascade = CascadeType.ALL)
    private User user;
}

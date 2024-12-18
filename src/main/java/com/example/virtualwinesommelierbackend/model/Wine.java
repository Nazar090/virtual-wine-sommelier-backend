package com.example.virtualwinesommelierbackend.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

// Represents a wine entity in the system. It stores all wine data
@Entity
@Getter
@Setter
@Table(name = "wines")
public class Wine {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    @Length(max = 50)
    private String name;
    @Column(nullable = false)
    @Length(max = 50)
    private String type;
    @Column(nullable = false)
    @Length(max = 50)
    private String color;
    @Column(nullable = false)
    @Length(max = 50)
    private String strength;
    @Column(nullable = false)
    @Length(max = 50)
    private String country;
    @Column(nullable = false)
    @Length(max = 50)
    private String grape;
    @Column(nullable = false)
    @Min(0)
    private BigDecimal price;
    @Column(nullable = false)
    @Length(max = 1500)
    private String description;
}

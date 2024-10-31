package com.example.virtualwinesommelierbackend.repository;

import com.example.virtualwinesommelierbackend.model.Wine;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WineRepository extends JpaRepository<Wine, Long> {
}

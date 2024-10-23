package com.example.virtualwinesommelierbackend.repository;

import com.example.virtualwinesommelierbackend.model.Address;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<Address, Long> {

}

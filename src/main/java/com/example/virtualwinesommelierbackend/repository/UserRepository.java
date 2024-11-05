package com.example.virtualwinesommelierbackend.repository;

import com.example.virtualwinesommelierbackend.model.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    /**
     * Checks if a user with the specified email exists in the database.
     *
     * @param email the email address to check
     * @return true if a user with the specified email exists, false otherwise
     */
    boolean existsByEmail(String email);

    /**
     * Retrieves a user by email, loading their associated roles eagerly.
     *
     * @param email the email address of the user to retrieve
     * @return an Optional containing the User if found, or empty if not found
     */
    @EntityGraph(attributePaths = "roles")
    Optional<User> findByEmail(String email);
}

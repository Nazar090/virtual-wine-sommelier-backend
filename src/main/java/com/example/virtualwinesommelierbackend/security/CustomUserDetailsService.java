package com.example.virtualwinesommelierbackend.security;

import com.example.virtualwinesommelierbackend.exception.EntityNotFoundException;
import com.example.virtualwinesommelierbackend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Custom implementation of {@link UserDetailsService} that loads user-specific data
 * during authentication
 * This service interacts with {@link UserRepository} to fetch user data from the storage.
 */
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    /**
     * Locates the user based on the email provided.
     *
     * @param email The user's login email address
     * @return The {@link UserDetails} object containing user information
     * @throws UsernameNotFoundException if the user with a given email cannot be found
     * @throws EntityNotFoundException if no user with the given email exists
     */
    @Override
    public UserDetails loadUserByUsername(String email)
            throws UsernameNotFoundException {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Can't find user by email: " + email));
    }
}

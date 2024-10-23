package com.example.virtualwinesommelierbackend.service.impl;

import com.example.virtualwinesommelierbackend.dto.user.UserRegistrationDto;
import com.example.virtualwinesommelierbackend.dto.user.UserRegistrationRequestDto;
import com.example.virtualwinesommelierbackend.exception.EntityNotFoundException;
import com.example.virtualwinesommelierbackend.exception.RegistrationException;
import com.example.virtualwinesommelierbackend.mapper.UserMapper;
import com.example.virtualwinesommelierbackend.model.Role;
import com.example.virtualwinesommelierbackend.model.User;
import com.example.virtualwinesommelierbackend.repository.RoleRepository;
import com.example.virtualwinesommelierbackend.repository.UserRepository;
import com.example.virtualwinesommelierbackend.service.UserService;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
/**
 * Implementation of the {@link UserService} for handling user registration and related operations.
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    /**
     * Registers a new user by validating the request, encoding the password, assigning the default role,
     * and saving the user in the repository.
     *
     * @param requestDto The user registration request data, including email, password, and other details.
     * @return A {@link UserRegistrationDto} containing the registered user's details.
     * @throws RegistrationException if a user with the given email already exists.
     * @throws EntityNotFoundException if the default USER role is not found in the database.
     */
    @Override
    public UserRegistrationDto register(UserRegistrationRequestDto requestDto)
            throws RegistrationException {
        if (userRepository.existsByEmail(requestDto.getEmail())) {
            throw new RegistrationException("User with this email already exists");
        }
        User user = userMapper.toUserEntity(requestDto);
        Role role = roleRepository.findByRole(Role.RoleName.USER)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Can't find role by role name " + Role.RoleName.USER.name()));
        user.setRoles(Set.of(role));
        user.setPassword(passwordEncoder.encode(requestDto.getPassword()));
        userRepository.save(user);
        // register shopping cart here(later)

        return userMapper.toDto(user);
    }
}

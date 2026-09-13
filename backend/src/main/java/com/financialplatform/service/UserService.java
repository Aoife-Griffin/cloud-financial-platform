package com.financialplatform.service;

import com.financialplatform.dto.CreateUserRequest;
import com.financialplatform.dto.UserResponse;
import com.financialplatform.model.User;
import com.financialplatform.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UserResponse createUser(CreateUserRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new RuntimeException("Email address already registered");
        }

        User user = new User();
        user.setEmail(request.email());
        /// Uses Bcrypt to hash password before saving
        user.setPasswordHash(passwordEncoder.encode(request.password())); 

        User savedUser = userRepository.save(user);
        return new UserResponse(savedUser.getId(), savedUser.getEmail(), savedUser.getCreatedAt());
    }

    public UserResponse getUserById(Long id) {
        User user = userRepository.findById(id).orElse(null);
        if (user == null) {
            throw new RuntimeException("User not found with ID: " + id);
        }
        return new UserResponse(user.getId(), user.getEmail(), user.getCreatedAt());
    }
}

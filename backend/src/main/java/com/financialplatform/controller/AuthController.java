package com.financialplatform.controller;

import com.financialplatform.dto.CreateUserRequest;
import com.financialplatform.dto.UserResponse;
import com.financialplatform.dto.AuthResponse;
import com.financialplatform.model.User;
import com.financialplatform.repository.UserRepository;
import com.financialplatform.service.UserService;
import com.financialplatform.security.JwtService;
import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthController(UserService userService, UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.userService = userService;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(@Valid @RequestBody CreateUserRequest request) {
        UserResponse response = userService.createUser(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody CreateUserRequest request) {
        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new RuntimeException("Invalid email or password "));

    
        if (!passwordEncoder.matches(request.password(), user.getPasswordHash())) {
            throw new RuntimeException("Invalid email or password ");
        }

        
        String token = jwtService.generateToken(user.getId(), user.getEmail());
        return ResponseEntity.ok(new AuthResponse(token, user.getEmail()));
    }
}

@RestController
@RequestMapping("/api")
@Tag(name = "Transactions", description = "Transaction history management endpoints")
public class TransactionController {

    @Operation(summary = "Get user transactions", description = "Gets filtered, sorted, and paginated transaction arrays for the authenticated profile.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Success"),
        @ApiResponse(responseCode = "401", description = "Invalid or expired JWT credentials"),
        @ApiResponse(responseCode = "500", description = "Internal data layer failure")
    })
    @GetMapping("/transactions")
    public ResponseEntity<?> getTransactions(
            @RequestParam(required = false) String search,
            @RequestParam(defaultValue = "1") int page) {
        // Implementation
        return ResponseEntity.ok().build();
    }
}

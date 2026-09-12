package com.financialplatform.repository;

import com.financialplatform.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    // unique helper to check if email already exists
    boolean existsByEmail(String email);
    Optional<User> findByEmail(String email);
}

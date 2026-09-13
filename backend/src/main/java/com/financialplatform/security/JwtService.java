package com.financialplatform.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;
import java.util.Date;
import javax.crypto.SecretKey;

@Service
public class JwtService {

    /// Using secretkey for signing and verifying tokens. 
    private static final String SECRET_STRING = "your-super-secret-secure-key-must-be-at-least-256-bits-long-cloud-financial-platform";
    private final SecretKey key = Keys.hmacShaKeyFor(SECRET_STRING.getBytes());
    /// Had to look up 24 hours in milliseconds
    private static final long EXPIRATION_TIME = 86400000;

    public String generateToken(Long userId, String email) {
        return Jwts.builder()
                .subject(email)
                .claim("userId", userId) 
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(key)
                .compact();
    }

    public Long extractUserId(String token) {
        return extractAllClaims(token).get("userId", Long.class);
    }
    
    // Keep your existing extractEmail, isTokenValid, and extractAllClaims methods exactly as they are...

    /// Gets email from token
    public String extractEmail(String token) {
        return extractAllClaims(token).getSubject();
    }

    /// Checks if token is expired
    public boolean isTokenValid(String token) {
        try {
            return !extractAllClaims(token).getExpiration().before(new Date());
        } catch (Exception e) {
            return false;
        }
    }

    /// Gets claims
    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}

package com.yuosef.springbootstartertemplate.Services.Impl;

import com.yuosef.springbootstartertemplate.Daos.RefreshTokenDao;
import com.yuosef.springbootstartertemplate.Models.RefreshToken;
import com.yuosef.springbootstartertemplate.Models.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenServiceImpl {

    private final RefreshTokenDao refreshTokenRepository;

    @Value("${application.security.jwt.refresh-expiration}")
    private long refreshExpiration; // in milliseconds

    // Create a new refresh token for the user
    @Transactional
    public RefreshToken createRefreshToken(User user) {
        // delete old one first so only one refresh token per user exists
        refreshTokenRepository.deleteByUser(user);

        RefreshToken refreshToken = RefreshToken.builder()
                .user(user)
                .token(UUID.randomUUID().toString()) // random unique string
                .expiryDate(Instant.now().plusMillis(refreshExpiration))
                .build();

        return refreshTokenRepository.save(refreshToken);
    }

    // Find and validate the refresh token
    public RefreshToken verifyRefreshToken(String token) {
        RefreshToken refreshToken = refreshTokenRepository.findByToken(token)
                .orElseThrow(() -> new IllegalArgumentException("Refresh token not found"));

        if (refreshToken.isExpired()) {
            refreshTokenRepository.delete(refreshToken);
            throw new IllegalArgumentException("Refresh token expired, please login again");
        }

        return refreshToken;
    }

    // Called on logout
    @Transactional
    public void deleteByUser(User user) {
        refreshTokenRepository.deleteByUser(user);
    }
}

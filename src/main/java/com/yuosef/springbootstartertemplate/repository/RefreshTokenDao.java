package com.yuosef.springbootstartertemplate.repository;

import com.yuosef.springbootstartertemplate.models.RefreshToken;
import com.yuosef.springbootstartertemplate.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefreshTokenDao extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByToken(String token);

    // delete old refresh token when user logs in again
    void deleteByUser(User user);
}
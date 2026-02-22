package com.yuosef.springbootstartertemplate.Daos;

import com.yuosef.springbootstartertemplate.Models.RefreshToken;
import com.yuosef.springbootstartertemplate.Models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefreshTokenDao extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByToken(String token);

    // delete old refresh token when user logs in again
    void deleteByUser(User user);
}
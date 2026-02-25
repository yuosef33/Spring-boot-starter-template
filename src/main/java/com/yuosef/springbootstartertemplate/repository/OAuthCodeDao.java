package com.yuosef.springbootstartertemplate.repository;

import com.yuosef.springbootstartertemplate.models.OAuthCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface OAuthCodeDao extends JpaRepository<OAuthCode, Long> {
    Optional<OAuthCode> findByCode(String code);
    void deleteByEmail(String email);
}

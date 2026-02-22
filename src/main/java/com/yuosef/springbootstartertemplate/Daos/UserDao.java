package com.yuosef.springbootstartertemplate.Daos;

import com.yuosef.springbootstartertemplate.Models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserDao extends JpaRepository<User,Long> {
    Optional<User> findUserByEmail(String email);
}

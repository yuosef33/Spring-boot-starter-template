package com.yuosef.springbootstartertemplate.Daos;

import com.yuosef.springbootstartertemplate.Models.Authority;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AuthorityDao extends JpaRepository<Authority,Long> {
   Optional<Authority> findByUserRole(String userRole);
    boolean existsByUserRole(String userRole);

}

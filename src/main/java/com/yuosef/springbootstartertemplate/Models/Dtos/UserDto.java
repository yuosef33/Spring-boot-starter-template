package com.yuosef.springbootstartertemplate.Models.Dtos;


import com.yuosef.springbootstartertemplate.Models.Authority;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {

    private Long Id;

    private String name;

    private String email;

    private String mobileNumber;

    private String pwd;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    List<Authority> authorities;

    @Enumerated(EnumType.STRING)
    private AuthProvider authProvider;
}

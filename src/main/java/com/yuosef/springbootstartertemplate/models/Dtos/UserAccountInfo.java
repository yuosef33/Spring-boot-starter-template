package com.yuosef.springbootstartertemplate.models.Dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;


public record UserAccountInfo (

    @NotBlank(message = "Name must not be blank")
    String name,

    @NotBlank(message = "Email must not be blank")
    @Email(message = "Email must be valid")
    String email,

    @NotBlank(message = "Password must not be blank")
    @Size(min = 8, message = "Password must be at least 8 characters")
    String password,

    @NotBlank(message = "Phone number must not be blank")
    @Pattern(regexp = "^\\+?[0-9]{10,15}$", message = "Phone number must be valid")
    String phoneNumber
)
{}

package com.yuosef.springbootstartertemplate.models.Dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record LoginInfo(@NotBlank(message = "Email must not be blank")
                        @Email(message = "Email must be valid")
                        String email,

                        @NotBlank(message = "Password must not be blank")
                        String password
){}

package com.yuosef.springbootstartertemplate.Models.Dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserAccountInfo {

    @NotBlank
    private String user_name;
    @NotBlank(message = "email must not be blank")
    @Email
    private String user_email;
    @NotBlank
    private String user_password;
    @NotBlank
    private String user_phoneNumber;

}

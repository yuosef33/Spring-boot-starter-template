package com.yuosef.springbootstartertemplate.Models.Dtos;


import com.yuosef.springbootstartertemplate.Models.Authority;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

    private Date createDt;

    List<Authority> authorities;
}

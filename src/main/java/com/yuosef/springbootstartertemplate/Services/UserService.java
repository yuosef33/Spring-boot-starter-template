package com.yuosef.springbootstartertemplate.Services;

import com.yuosef.springbootstartertemplate.Models.Dtos.LoginInfo;
import com.yuosef.springbootstartertemplate.Models.Dtos.UserAccountInfo;
import com.yuosef.springbootstartertemplate.Models.Dtos.UserDto;
import com.yuosef.springbootstartertemplate.Models.User;
import jakarta.transaction.SystemException;

public interface UserService {
    String login(LoginInfo loginInfo);
    UserDto createUser(UserAccountInfo clientAccInfo) throws SystemException;
    User getUserByEmail(String email) throws SystemException;

}

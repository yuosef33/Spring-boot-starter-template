package com.yuosef.springbootstartertemplate.services;

import com.yuosef.springbootstartertemplate.models.Dtos.*;
import com.yuosef.springbootstartertemplate.models.User;
import jakarta.transaction.SystemException;

public interface UserService {
    AuthResponse login(LoginInfo loginInfo);
    UserDto createUser(UserAccountInfo clientAccInfo) throws SystemException;
    User getUserByEmail(String email) throws SystemException;
    AuthResponse refreshToken(RefreshTokenRequest request);
    void logout(User user);
    AuthResponse exchangeCode(String code);
}

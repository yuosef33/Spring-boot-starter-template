package com.yuosef.springbootstartertemplate.Services;

import com.yuosef.springbootstartertemplate.Models.Dtos.*;
import com.yuosef.springbootstartertemplate.Models.User;
import jakarta.transaction.SystemException;

public interface UserService {
    AuthResponse login(LoginInfo loginInfo);
    UserDto createUser(UserAccountInfo clientAccInfo) throws SystemException;
    User getUserByEmail(String email) throws SystemException;
    AuthResponse refreshToken(RefreshTokenRequest request);
    void logout(User user);
}

package com.yuosef.springbootstartertemplate.Services.Impl;

import com.yuosef.springbootstartertemplate.Config.JWT.TokenHandler;
import com.yuosef.springbootstartertemplate.Daos.AuthorityDao;
import com.yuosef.springbootstartertemplate.Daos.UserDao;
import com.yuosef.springbootstartertemplate.Models.Authority;
import com.yuosef.springbootstartertemplate.Models.Dtos.*;
import com.yuosef.springbootstartertemplate.Models.Mappers.Usermapper;
import com.yuosef.springbootstartertemplate.Models.RefreshToken;
import com.yuosef.springbootstartertemplate.Models.User;
import com.yuosef.springbootstartertemplate.Services.UserService;
import jakarta.transaction.SystemException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserDao userDao;
    private final PasswordEncoder passwordEncoder;
    private final TokenHandler tokenHandler;
    private final AuthorityDao authorityRepository;
    private final AuthenticationManager authenticationManager;
    private final RefreshTokenServiceImpl refreshTokenService;



    @Override
    public AuthResponse login(LoginInfo loginInfo) {
         authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginInfo.email(), loginInfo.password()));
        User user = userDao.findUserByEmail(loginInfo.email()).orElseThrow();
        String accessToken = tokenHandler.createToken(user);
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(user);
        return new AuthResponse(accessToken,refreshToken.getToken());
    }

    @Override
    public UserDto createUser(UserAccountInfo userAccInfo) throws SystemException {
        Optional<User> user = userDao.findUserByEmail(userAccInfo.name());
        if (user.isPresent())
            throw new SystemException("this email " + userAccInfo.name() + " is already in use");
        // client not exist
        Authority userRole = authorityRepository.findByUserRole("USER")
                .orElseThrow(() -> new IllegalStateException("Role USER not found"));

        List<Authority> auths = List.of(userRole);

        User user2 = User.builder()
                .name(userAccInfo.name())
                .email(userAccInfo.email())
                .mobileNumber(userAccInfo.phoneNumber())
                .pwd(passwordEncoder.encode(userAccInfo.password()))
                .createDt(new Date(System.currentTimeMillis()))
                .authorities(auths).build();
        return Usermapper.toDto(userDao.save(user2));
    }

    @Override
    public User getUserByEmail(String email) throws SystemException {
        Optional<User> user = userDao.findUserByEmail(email);
        return user.orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }
    @Override
    public AuthResponse refreshToken(RefreshTokenRequest request) {
        // 1. verify the refresh token is valid and not expired
        RefreshToken refreshToken = refreshTokenService.verifyRefreshToken(request.refreshToken());

        // 2. get the user from the refresh token
        User user = refreshToken.getUser();

        // 3. generate a new access token
        String newAccessToken = tokenHandler.createToken(user);

        // 4. return new access token + same refresh token
        return new AuthResponse(newAccessToken, refreshToken.getToken());
    }
    @Override
    public void logout(User user) {
        refreshTokenService.deleteByUser(user);
    }

}

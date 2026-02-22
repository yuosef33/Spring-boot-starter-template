package com.yuosef.springbootstartertemplate.Services.Impl;

import com.yuosef.springbootstartertemplate.Config.JWT.TokenHandler;
import com.yuosef.springbootstartertemplate.Daos.AuthorityDao;
import com.yuosef.springbootstartertemplate.Daos.UserDao;
import com.yuosef.springbootstartertemplate.Models.Authority;
import com.yuosef.springbootstartertemplate.Models.Dtos.LoginInfo;
import com.yuosef.springbootstartertemplate.Models.Dtos.UserAccountInfo;
import com.yuosef.springbootstartertemplate.Models.Dtos.UserDto;
import com.yuosef.springbootstartertemplate.Models.Mappers.Usermapper;
import com.yuosef.springbootstartertemplate.Models.User;
import com.yuosef.springbootstartertemplate.Services.UserService;
import jakarta.transaction.SystemException;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

@Service
public class UserServiceImpl implements UserService {

    private final UserDao userDao;
    private final PasswordEncoder passwordEncoder;
    private final TokenHandler tokenHandler;
    private final AuthorityDao authorityRepository;
    private final AuthenticationManager authenticationManager;

    public UserServiceImpl(UserDao userDao, PasswordEncoder passwordEncoder, TokenHandler tokenHandler, AuthorityDao authorityRepository, AuthenticationManager authenticationManager) {
        this.userDao = userDao;
        this.passwordEncoder = passwordEncoder;
        this.tokenHandler = tokenHandler;
        this.authorityRepository = authorityRepository;
        this.authenticationManager = authenticationManager;
    }


    @Override
    public String login(LoginInfo loginInfo) {
         authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginInfo.email(), loginInfo.password()));
        User user = userDao.findUserByEmail(loginInfo.email()).orElseThrow();
        return tokenHandler.createToken(user);
    }

    @Override
    public UserDto createUser(UserAccountInfo userAccInfo) throws SystemException {
        Optional<User> user = userDao.findUserByEmail(userAccInfo.getUser_email());
        if (user.isPresent())
            throw new SystemException("this email " + userAccInfo.getUser_email() + " is already in use");
        // client not exist
        Authority userRole = authorityRepository.findByUserRole("USER")
                .orElseThrow(() -> new IllegalStateException("Role USER not found"));

        List<Authority> auths = List.of(userRole);

        User user2 = User.builder()
                .name(userAccInfo.getUser_name())
                .email(userAccInfo.getUser_email())
                .mobileNumber(userAccInfo.getUser_phoneNumber())
                .pwd(passwordEncoder.encode(userAccInfo.getUser_password()))
                .createDt(new Date(System.currentTimeMillis()))
                .authorities(auths).build();
        return Usermapper.toDto(userDao.save(user2));
    }

    @Override
    public User getUserByEmail(String email) throws SystemException {
        Optional<User> user = userDao.findUserByEmail(email);
        return user.orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }


}

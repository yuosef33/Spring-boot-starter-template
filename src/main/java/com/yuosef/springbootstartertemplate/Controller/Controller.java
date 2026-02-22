package com.yuosef.springbootstartertemplate.Controller;

import com.yuosef.springbootstartertemplate.Models.Dtos.*;
import com.yuosef.springbootstartertemplate.Services.UserService;
import jakarta.transaction.SystemException;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/auth")
public class Controller {

    private final UserService userService;

    public Controller(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/createUser")
    public ResponseEntity<UserDto> createUser(@Valid @RequestBody UserAccountInfo userAccountInfo) throws SystemException {
         UserDto user= userService.createUser(userAccountInfo);
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }
    @PostMapping("/Login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginInfo loginInfo){
        return ResponseEntity.ok(userService.login(loginInfo));
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<AuthResponse> refresh(@RequestBody RefreshTokenRequest request) {
        return ResponseEntity.ok(userService.refreshToken(request));
    }


}

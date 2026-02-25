package com.yuosef.springbootstartertemplate.controller;

import com.yuosef.springbootstartertemplate.models.Dtos.*;
import com.yuosef.springbootstartertemplate.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.transaction.SystemException;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


@RestController
@RequestMapping("/auth")
@Tag(name = "Authentication", description = "Register, Login, Google-Oauth, Refresh Token, Logout")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @Operation(summary = "Register a new user")
    @PostMapping("/createUser")
    public ResponseEntity<ApiResponse<UserDto>> createUser(@Valid @RequestBody UserAccountInfo userAccountInfo) throws SystemException {
         UserDto user= userService.createUser(userAccountInfo);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.ok("User Created successfully",user));
    }
    @PostMapping("/Login")
    public ResponseEntity<ApiResponse<AuthResponse>> login(@Valid @RequestBody LoginInfo loginInfo){
        return ResponseEntity.ok(ApiResponse.ok("Login successful",userService.login(loginInfo)));
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<ApiResponse<AuthResponse>> refresh(@RequestBody RefreshTokenRequest request) {
        return ResponseEntity.ok(ApiResponse.ok("Token Refreshed",userService.refreshToken(request)));
    }

    @GetMapping("/oauth2/callback")
    public ResponseEntity<ApiResponse<?>> callback(
            @RequestParam String code) {
        return ResponseEntity.ok(ApiResponse.ok(
                "Code generated, call /auth/exchange-code before 60 seconds to get your tokens ",
                Map.of("code", code)
        ));
    }

    @PostMapping("/exchange-code")
    public ResponseEntity<ApiResponse<AuthResponse>> exchangeCode(@RequestParam String code) {
        return ResponseEntity.ok(ApiResponse.ok(
                "Tokens generated successfully",
                userService.exchangeCode(code)
        ));
    }
}

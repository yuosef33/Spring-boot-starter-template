package com.yuosef.springbootstartertemplate.Controller;

import com.yuosef.springbootstartertemplate.Models.Dtos.*;
import com.yuosef.springbootstartertemplate.Services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.transaction.SystemException;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/auth")
@Tag(name = "Authentication", description = "Register, Login, Refresh Token, Logout")
public class Controller {

    private final UserService userService;

    public Controller(UserService userService) {
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



}

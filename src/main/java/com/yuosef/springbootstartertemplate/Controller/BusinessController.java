package com.yuosef.springbootstartertemplate.Controller;

import com.yuosef.springbootstartertemplate.Models.Dtos.ApiResponse;
import com.yuosef.springbootstartertemplate.Models.User;
import com.yuosef.springbootstartertemplate.Services.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/business")
@Tag(name = "BusinessController", description = "Your business endpoints here")
public class BusinessController {

    private final UserService userService;

    public BusinessController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/hello") /// this path is Authenticated
    public ResponseEntity<ApiResponse<String>> hello() {
        return ResponseEntity.ok(ApiResponse.ok("Hello, World!"));
    }
    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logout(@AuthenticationPrincipal User user) {
        userService.logout(user);
        return ResponseEntity.ok(ApiResponse.ok("Logged out successfully"));
    }
}

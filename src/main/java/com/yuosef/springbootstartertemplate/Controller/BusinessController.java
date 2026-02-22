package com.yuosef.springbootstartertemplate.Controller;

import com.yuosef.springbootstartertemplate.Models.User;
import com.yuosef.springbootstartertemplate.Services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/business")
public class BusinessController {

    private final UserService userService;

    public BusinessController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/hello") /// this path is Authenticated
    public String hello() {
        return "Hello, World!";
    }
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@AuthenticationPrincipal User user) {
        userService.logout(user);
        return ResponseEntity.noContent().build();
    }
}

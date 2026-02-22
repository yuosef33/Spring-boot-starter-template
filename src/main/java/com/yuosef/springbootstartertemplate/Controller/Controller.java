package com.yuosef.springbootstartertemplate.Controller;

import com.yuosef.springbootstartertemplate.Models.Dtos.LoginInfo;
import com.yuosef.springbootstartertemplate.Models.Dtos.UserAccountInfo;
import com.yuosef.springbootstartertemplate.Models.Dtos.UserDto;
import com.yuosef.springbootstartertemplate.Services.UserService;
import jakarta.transaction.SystemException;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class Controller {

    private final UserService userService;

    public Controller(UserService userService) {
        this.userService = userService;
    }
    @GetMapping("/hello")
    public String hello() {
        return "Hello, World!";
    }
    @PostMapping("/createUser")
    public ResponseEntity<UserDto> createUser(@Valid @RequestBody UserAccountInfo userAccountInfo) throws SystemException {
         UserDto user= userService.createUser(userAccountInfo);
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }
    @PostMapping("/Login")
    public ResponseEntity<Map<String,String>> login(@RequestBody LoginInfo loginInfo){
        return ResponseEntity.ok(new HashMap<>(Map.of("Token",userService.login(loginInfo))));
    }

}

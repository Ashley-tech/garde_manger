package com.example.garde_manger_back.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.garde_manger_back.config.AuthService;
import com.example.garde_manger_back.dto.LoginRequest;
import com.example.garde_manger_back.dto.LoginResponse;

@RestController
@RequestMapping("/login")
@CrossOrigin
public class LoginController {
    private final AuthService authService;

    public LoginController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping
    public LoginResponse login(@RequestBody LoginRequest request) {
        return authService.authenticate(request.email, request.password);
    }
    
}

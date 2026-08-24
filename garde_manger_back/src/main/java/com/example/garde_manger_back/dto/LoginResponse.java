package com.example.garde_manger_back.dto;

public class LoginResponse {
    public boolean success;
    public String message;
    public Integer userId;

    public LoginResponse(boolean success, String message, Integer userId) {
        this.success = success;
        this.message = message;
        this.userId = userId;
    }
}

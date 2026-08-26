package com.example.garde_manger_back.dto;

public class EmailResponse {
    private boolean success;
    private String message;

    public EmailResponse(boolean success, String message){
        this.success = success;
        this.message = message;
    }

    public void setSuccess(boolean success){
        this.success = success;
    }

    public boolean getSuccess(){
        return success;
    }

    public String getMessage(){
        return message;
    }

    public void setMessage(String message){
        this.message = message;
    }
}
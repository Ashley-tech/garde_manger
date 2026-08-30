package com.example.garde_manger_back.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.garde_manger_back.config.EmailService;
import com.example.garde_manger_back.dto.EmailBodyRequest;
import com.example.garde_manger_back.dto.EmailResponse;

@RestController
public class NotificationController {

    private final EmailService emailService;

    public NotificationController(EmailService emailService) {
        this.emailService = emailService;
    }

    @PostMapping("/sendEmail")
    public EmailResponse send(
            @RequestBody EmailBodyRequest request) {

        EmailResponse sent = emailService.sendEmail(
                //request.getFrom(),
                request.getTo(),
                request.getSubject(),
                request.getText()
        );

        if (sent.getSuccess()) {
            return new EmailResponse(true,"Messge envoyé");
        }

        return new EmailResponse(false, "Erreur lors de l'envoi du mail");
    }
}
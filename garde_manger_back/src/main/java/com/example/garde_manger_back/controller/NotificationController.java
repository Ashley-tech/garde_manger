package com.example.garde_manger_back.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<?> send(@RequestBody EmailBodyRequest request) {

        EmailResponse sent = emailService.sendEmail(
                //request.getFrom(),
                request.getTo(),
                request.getSubject(),
                request.getText()
        );

        if (sent.getSuccess() == true) {
            return ResponseEntity.ok("Email envoyé !");
        }

        return ResponseEntity.internalServerError()
                .body("Erreur lors de l'envoi de l'email.");
    }
}
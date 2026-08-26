package com.example.garde_manger_back.config;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.example.garde_manger_back.dto.EmailResponse;

import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public EmailResponse sendEmail(String to, String subject, String text) {
        EmailResponse er;

        try {
            String cleanedText = text.replace("\r", "").trim();
            String html = cleanedText.replace("\n", "<br>");

            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(cleanedText, html);

            mailSender.send(message);
            er = new EmailResponse(true,"Email envoyé !");

            System.out.println(er);
            return er;

        } catch (jakarta.mail.MessagingException | org.springframework.mail.MailException e) {
            er = new EmailResponse(false,"Erreur lors de l'envoi de l'email : "+ e.getMessage());
            System.err.println(er);
            return er;
        }
    }
}

package com.murilodias03.bookstore.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.murilodias03.bookstore.config.EmailConfig;
import com.murilodias03.bookstore.data.dto.request.EmailRequestDTO;
import com.murilodias03.bookstore.mail.EmailSender;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Service
public class EmailService {

    private final EmailSender emailSender;
    private final EmailConfig emailConfigs;

    public EmailService(EmailSender emailSender,
                        EmailConfig emailConfigs) {
        this.emailSender = emailSender;
        this.emailConfigs = emailConfigs;
    }

    public void sendSimpleEmail(EmailRequestDTO emailRequest) {
        emailSender
                .to(emailRequest.getTo())
                .withSubject(emailRequest.getSubject())
                .withMessage(emailRequest.getBody())
                .send(emailConfigs);
    }

    public void sendEmailWithAttachment(String emailRequestJson, MultipartFile attachment) {
        File tempFile = null;

        try {
            EmailRequestDTO emailRequest = new ObjectMapper().readValue(emailRequestJson, EmailRequestDTO.class);
            tempFile = File.createTempFile("attachment", attachment.getOriginalFilename());
            attachment.transferTo(tempFile);

            emailSender
                    .to(emailRequest.getTo())
                    .withSubject(emailRequest.getSubject())
                    .withMessage(emailRequest.getBody())
                    .attach(tempFile.getAbsolutePath())
                    .send(emailConfigs);

        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error parsing e-mail request!", e);
        } catch (IOException e) {
            throw new RuntimeException("Error processing the attachment!", e);
        } finally {
            if (tempFile != null && tempFile.exists()) tempFile.delete();
        }
    }
}
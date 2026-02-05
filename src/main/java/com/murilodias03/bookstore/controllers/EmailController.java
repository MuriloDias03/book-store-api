package com.murilodias03.bookstore.controllers;

import com.murilodias03.bookstore.controllers.docs.EmailControllerDocs;
import com.murilodias03.bookstore.data.dto.request.EmailRequestDTO;
import com.murilodias03.bookstore.services.EmailService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/email")
public class EmailController implements EmailControllerDocs {

    private final EmailService emailService;

    public EmailController(EmailService emailService) {
        this.emailService = emailService;
    }

    @PostMapping
    @Override
    public ResponseEntity<String> sendEmail(@RequestBody EmailRequestDTO emailRequest) {
        emailService.sendSimpleEmail(emailRequest);
        return new ResponseEntity<>("E-mail sent with success!", HttpStatus.OK);
    }

    @PostMapping(value = "/withAttachment",
                 consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Override
    public ResponseEntity<String> sendEmailWithAttachment(@RequestParam String emailRequest,
                                                          @RequestParam MultipartFile attachment) {
        emailService.sendEmailWithAttachment(emailRequest, attachment);
        return new ResponseEntity<>("E-mail with attachment sent with success!", HttpStatus.OK);
    }
}
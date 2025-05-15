package com.app.vdsp.service;

import jakarta.mail.MessagingException;

public interface EmailService {
    void sendEmailWithAttachment(String to, String subject, String body, byte[] attachment, String filename) throws MessagingException;
}
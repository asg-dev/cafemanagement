package com.org.cafemgmt.service;

import javax.mail.MessagingException;

public interface EmailService {
    public void sendMessage(String to, String subject, String text) throws MessagingException;
}

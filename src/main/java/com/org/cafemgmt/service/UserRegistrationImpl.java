package com.org.cafemgmt.service;

import java.util.UUID;

public class UserRegistrationImpl implements UserRegistration {
    @Override
    public String generateRegistrationMessage(UUID token, String userName) {
        String message = "Hello, " + userName + "!<br><br>You've successfully registered to Freshbrew Cafe Management! Please click on this URL to setup your password: " +
                "http://localhost:8080/register_account/" + token + ". <br><br>Thank you,<br>Freshbrew Management";
        return message;
    }

    @Override
    public String generatePasswordResetMessage(UUID token, String userName) {
        String message = "Hello, " + userName + "!<br><br>We've received a request to reset your password. Please click on this URL to setup your password: " +
                "http://localhost:8080/register_account/" + token + ". <br><br>Thank you,<br>Freshbrew Management";
        return message;
    }
}

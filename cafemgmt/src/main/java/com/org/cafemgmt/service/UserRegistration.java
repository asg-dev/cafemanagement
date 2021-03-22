package com.org.cafemgmt.service;

import java.util.UUID;

public interface UserRegistration {
    public String generateRegistrationMessage(UUID token, String userName);
}

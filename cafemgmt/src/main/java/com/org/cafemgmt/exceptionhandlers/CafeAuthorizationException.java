package com.org.cafemgmt.exceptionhandlers;

public class CafeAuthorizationException extends RuntimeException {
    public CafeAuthorizationException(String exception) {
        super(exception);
    }
}

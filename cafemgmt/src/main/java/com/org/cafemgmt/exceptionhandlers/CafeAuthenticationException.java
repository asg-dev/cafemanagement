package com.org.cafemgmt.exceptionhandlers;

public class CafeAuthenticationException extends RuntimeException {
    public CafeAuthenticationException(String exception) {
        super(exception);
    }
}

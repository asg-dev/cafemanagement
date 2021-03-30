package com.org.cafemgmt.exceptionhandlers;

public class CafeEntityNotFoundException extends RuntimeException {
    public CafeEntityNotFoundException(String exception) {
        super(exception);
    }
}

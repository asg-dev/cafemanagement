package com.org.cafemgmt.exceptionhandlers;

public class CafeInvalidParameterException extends RuntimeException {

    public CafeInvalidParameterException(String exception) {
        super(exception);
    }
}

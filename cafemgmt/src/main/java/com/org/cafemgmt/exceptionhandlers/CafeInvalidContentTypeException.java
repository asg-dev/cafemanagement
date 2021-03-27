package com.org.cafemgmt.exceptionhandlers;

public class CafeInvalidContentTypeException extends RuntimeException {
    public CafeInvalidContentTypeException(String exception) {
        super(exception);
    }
}

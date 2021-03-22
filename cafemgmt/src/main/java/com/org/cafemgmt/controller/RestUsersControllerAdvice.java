package com.org.cafemgmt.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.security.InvalidParameterException;

@ControllerAdvice
public class RestUsersControllerAdvice {
    @ExceptionHandler(InvalidParameterException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public Object processValidationError(InvalidParameterException invalidParameterException) {
        String result = invalidParameterException.getMessage();
        System.out.println("###########" + result);
        return invalidParameterException;
    }
}

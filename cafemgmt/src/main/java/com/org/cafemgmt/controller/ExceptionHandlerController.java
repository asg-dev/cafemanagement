package com.org.cafemgmt.controller;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ExceptionHandlerController {
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public String handleServerError() {
        return "inalidre";
    }
}

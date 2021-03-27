package com.org.cafemgmt.controller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;

@Controller
public class CafeErrorHandleController implements ErrorController {

    @RequestMapping(value = "/error")
    public String handleError(HttpServletRequest request) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        if (status != null) {
            Integer statusCode = Integer.valueOf(status.toString());

            if(statusCode == HttpStatus.NOT_FOUND.value()) {
                return "errorpages/404-error";
            }

            else if(statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
                return "errorpages/500-error";
            }

            else if(statusCode == HttpStatus.FORBIDDEN.value()) {
                return "errorpages/403-error";
            }
        }
        return "errorpages/error-fallback";
    }

    @Override
    public String getErrorPath() {
        return null;
    }
}

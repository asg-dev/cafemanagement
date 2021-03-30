package com.org.cafemgmt.config;

import com.org.cafemgmt.model.CafeErrorDetails;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

@Slf4j
@Component
public class CafeAuthenticationFailureHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {
        httpServletResponse.setStatus(HttpStatus.UNAUTHORIZED.value());
        log.error("AuthenticationException occurred");
        CafeErrorDetails cafeErrorDetails = new CafeErrorDetails(new Date(), e.getMessage(), HttpStatus.UNAUTHORIZED.value(), e.getMessage());
        httpServletResponse.getOutputStream().println(cafeErrorDetails.toString());
    }

}

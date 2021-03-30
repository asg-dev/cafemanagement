package com.org.cafemgmt.exceptionhandlers;

import com.org.cafemgmt.model.CafeErrorDetails;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Date;

@ControllerAdvice
public class CafeRestExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(CafeEntityNotFoundException.class)
    public final ResponseEntity<CafeErrorDetails> handleEntityNotFoundException(CafeEntityNotFoundException exception, WebRequest request) {
        CafeErrorDetails errorDetails = new CafeErrorDetails(new Date(), exception.getMessage(), HttpStatus.NOT_FOUND.value(), request.getDescription(false));
        return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(CafeInvalidParameterException.class)
    public final ResponseEntity<CafeErrorDetails> handleInvalidParameterException(CafeInvalidParameterException exception, WebRequest request) {
        CafeErrorDetails errorDetails = new CafeErrorDetails(new Date(), exception.getMessage(), HttpStatus.BAD_REQUEST.value(), request.getDescription(false));
        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(CafeInvalidContentTypeException.class)
    public final ResponseEntity<CafeErrorDetails> handleInvalidContentTypeException(CafeInvalidContentTypeException exception, WebRequest request) {
        CafeErrorDetails errorDetails = new CafeErrorDetails(new Date(), exception.getMessage(), HttpStatus.UNSUPPORTED_MEDIA_TYPE.value(), request.getDescription(false));
        return new ResponseEntity<>(errorDetails, HttpStatus.UNSUPPORTED_MEDIA_TYPE);
    }

    @ExceptionHandler(CafeAuthorizationException.class)
    public final ResponseEntity<CafeErrorDetails> handleAuthorizationException(CafeAuthorizationException exception, WebRequest request) {
        CafeErrorDetails errorDetails = new CafeErrorDetails(new Date(), exception.getMessage(), HttpStatus.FORBIDDEN.value(), request.getDescription(false));
        return new ResponseEntity<>(errorDetails, HttpStatus.FORBIDDEN);
    }

}

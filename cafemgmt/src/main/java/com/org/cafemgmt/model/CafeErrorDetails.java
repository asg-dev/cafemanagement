package com.org.cafemgmt.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class CafeErrorDetails {
    private Date timestamp;
    private String message;
    private long statusCode;
    private String details;

    public CafeErrorDetails(Date timestamp, String message, long statusCode, String details) {
        super();
        this.timestamp = timestamp;
        this.message = message;
        this.statusCode = statusCode;
        this.details = details;
    }
}

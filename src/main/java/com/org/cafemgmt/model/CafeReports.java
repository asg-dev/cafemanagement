package com.org.cafemgmt.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CafeReports {
    private String dateRange;
    private long customerId;
    private long approverId;
}

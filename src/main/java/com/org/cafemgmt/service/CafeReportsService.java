package com.org.cafemgmt.service;

import com.org.cafemgmt.model.CafeOrders;

import java.io.IOException;
import java.util.List;

public interface CafeReportsService {
    public String generateReportsCsv(List<CafeOrders> cafeOrdersList) throws IOException;
}

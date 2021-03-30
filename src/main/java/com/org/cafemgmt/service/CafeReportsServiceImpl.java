package com.org.cafemgmt.service;

import com.org.cafemgmt.model.CafeOrders;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class CafeReportsServiceImpl implements CafeReportsService {
    @Override
    public String generateReportsCsv(List<CafeOrders> cafeOrdersList) throws IOException {
        FileWriter file = createCsvFile(cafeOrdersList);
        return null;
    }
    public FileWriter createCsvFile(List<CafeOrders> cafeOrders) throws IOException {
        String[] csvHeader = { "#", "Order #", "Customer Name", "Total Price", "Rating" };
        FileWriter fileWriter = new FileWriter("testcsv.csv");
        CSVPrinter printer = new CSVPrinter(fileWriter, CSVFormat.DEFAULT.withHeader(csvHeader));
        long count = 1;
        double totalSaleReportPrice = 0.0;
        for (CafeOrders order : cafeOrders) {
            totalSaleReportPrice += order.getTotalPrice();
            printer.printRecord(count++, order.getId(), order.getCustomerName(), order.getTotalPrice(), order.getRating());
        }
        printer.printRecord(totalSaleReportPrice);
        File csvFile = new File("testcsv.csv");

        return fileWriter;
    }
}

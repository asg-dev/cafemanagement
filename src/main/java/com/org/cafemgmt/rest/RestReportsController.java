package com.org.cafemgmt.rest;

import com.org.cafemgmt.exceptionhandlers.CafeInvalidParameterException;
import com.org.cafemgmt.model.CafeOrders;
import com.org.cafemgmt.service.CafeOrderService;
import com.org.cafemgmt.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.xml.ws.Response;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class RestReportsController {
    @Autowired
    UserService userService;

    @Autowired
    CafeOrderService cafeOrderService;

    @RequestMapping(value = "/search/users", method = RequestMethod.POST)
    public ResponseEntity<Object> searchCustomerName(@RequestParam("customer") String isCustomer, @RequestParam("q") String queryString) {
        if (Boolean.parseBoolean(isCustomer)) {
            Map<Long, String> userMap = userService.searchUser(queryString, false);
            return ResponseEntity.status(200).body(userMap);
        } else {
            Map<Long, String> userMap = userService.searchUser(queryString, true);
            return ResponseEntity.status(200).body(userMap);
        }
    }

    @RequestMapping(value = "/api/generate_report", method = RequestMethod.POST)
    public ResponseEntity<Object> generateReportRest(@RequestParam(value = "dateRange", required = true) String dateRange,
                                                     @RequestParam(value = "customerId", required = false) String customerId,
                                                     @RequestParam(value = "approverId", required = false) String approverId) {
        if (dateRange != null) {
            if (! dateRange.contains(" - ") && (dateRange.split(" - ").length != 2)) {
                throw new CafeInvalidParameterException("Invalid Date Range format. Format: startDate - endDate");
            }
        }

        long cId = (customerId != null) ? Long.parseLong(customerId) : 0;
        long aId = (approverId != null) ? Long.parseLong(approverId) : 0;
        if (cId != 0) {
            if (! userService.findUserById(cId).isPresent()) {
                throw new CafeInvalidParameterException("No user exists with id: " + customerId);
            }
        }
        if (aId != 0) {
            if (! userService.findUserById(aId).isPresent()) {
                throw new CafeInvalidParameterException("No user exists with id: " + customerId);
            }
        }
        List<CafeOrders> cafeOrdersList = cafeOrderService.getOrdersForReport(dateRange, cId, aId);
        Map<String, String> resultMap = new HashMap<String, String>();
        resultMap.put("totalOrders", String.valueOf(cafeOrdersList.size()));
        resultMap.put("totalProfit", String.valueOf(getProfit(cafeOrdersList)));
        return ResponseEntity.status(200).body(resultMap);
    }

    public double getProfit(List<CafeOrders> cafeOrders) {
        if (cafeOrders != null) {
            double totalProfit = 0;
            for (CafeOrders order : cafeOrders) {
                totalProfit += order.getTotalPrice();
            }
            return totalProfit;
        }
        return 0;
    }

}

package com.org.cafemgmt.controller;

import com.org.cafemgmt.common.UserManagement;
import com.org.cafemgmt.model.CafeOrders;
import com.org.cafemgmt.model.CafeReports;
import com.org.cafemgmt.model.CafeUsers;
import com.org.cafemgmt.model.PrincipalUserDetails;
import com.org.cafemgmt.service.CafeOrderService;
import com.org.cafemgmt.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanReader;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;
import sun.awt.ModalExclude;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Controller
@Slf4j
public class ReportsController {

    @Autowired
    UserService userService;

    @Autowired
    CafeOrderService cafeOrderService;

    @RequestMapping(value = "/reports", method = RequestMethod.GET)
    public String getReports(Authentication authentication, Model model) {
        String loggedInUsername = UserManagement.getUserName(authentication, userService);

        List<CafeOrders> cafeOrdersList = cafeOrderService.getAllApprovedOrders();
        cafeOrderService.setMenuItemsInternal(cafeOrdersList);
        model.addAttribute("allOrders", cafeOrdersList);
        model.addAttribute("firstCharInUsername", loggedInUsername.charAt(0));
        model.addAttribute("username", loggedInUsername);
        model.addAttribute("totalSalePrice", cafeOrderService.getSaleTotalForReport(cafeOrdersList));
        model.addAttribute("totalSaleReport", cafeOrdersList.size());
        model.addAttribute("totalRatings", cafeOrderService.getTotalRatingsForReport(cafeOrdersList));
        model.addAttribute("cafeReport", new CafeReports());
        return "reports";
    }

    @RequestMapping(value = "/generate_report", method = RequestMethod.POST)
    public String generateReport(Authentication authentication, @ModelAttribute("cafeReport") CafeReports cafeReport, Model model) {
        String loggedInUsername = UserManagement.getUserName(authentication, userService);
        model.addAttribute("firstCharInUsername", loggedInUsername.charAt(0));
        model.addAttribute("username", loggedInUsername);

        String dateRange = cafeReport.getDateRange();
        long approverId = cafeReport.getApproverId();
        long customerId = cafeReport.getCustomerId();
        Optional<CafeUsers> cafeUsersOptional = userService.findUserById(customerId);
        if (cafeUsersOptional.isPresent()) {
            if (cafeUsersOptional.get().isInternalUser()) {
                customerId = userService.findUserByEmail("walkincustomerdefault@freshbrew.com").getId();
            }
        }
        List<CafeOrders> obtainedOrders = cafeOrderService.getOrdersForReport(dateRange, customerId, approverId);
        cafeOrderService.setMenuItemsInternal(obtainedOrders);
        model.addAttribute("allOrders", obtainedOrders);
        model.addAttribute("totalSalePrice", cafeOrderService.getSaleTotalForReport(obtainedOrders));
        model.addAttribute("totalSaleReport", obtainedOrders.size());
        model.addAttribute("totalRatings", cafeOrderService.getTotalRatingsForReport(obtainedOrders));
        System.out.println(" Date Range :: " + dateRange + " Customer ID :: " + customerId + " Approver ID ::" + approverId);
        model.addAttribute("dateValue", dateRange);
        model.addAttribute("approverId",approverId);
        model.addAttribute("customerId",customerId);
        model.addAttribute("cafeReport", cafeReport);
        return "reports";
    }

    @RequestMapping(value = "/reports/export_csv", method = RequestMethod.POST)
    public void exportAsCsv(HttpServletResponse response, @ModelAttribute("dateValue") String dateRange,
                            @ModelAttribute("customerId") String customerId,
                            @ModelAttribute("approverId") String approverId) throws IOException {
        response.setContentType("text/csv");
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
        String currentTime = dateFormatter.format(new Date());
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=users_" + currentTime + ".csv";
        response.setHeader(headerKey, headerValue);

        System.out.println("Sent for CSV Export: --> dateRange: " + dateRange + " ::: customerId: " + customerId + " ::: approverId: " + approverId);
        long internalCustomerId= 0;
        long internalApproverId=0;
        if(customerId!=null && !customerId.isEmpty()){
            internalCustomerId= Long.parseLong(customerId);
        }
        if(approverId!=null && !approverId.isEmpty()){
            internalApproverId = Long.parseLong(approverId);
        }
        List<CafeOrders> cafeOrdersList = null;
        if((dateRange==null || dateRange.isEmpty()) && internalCustomerId == 0 && internalApproverId == 0){
            cafeOrdersList = cafeOrderService.listAllOrders();
        }else {
            cafeOrdersList = cafeOrderService.getOrdersForReport(dateRange, internalCustomerId, internalApproverId);
        }
        System.out.println(cafeOrdersList);
        for (CafeOrders order : cafeOrdersList) {
            if( userService.findUserById(order.getCustomerId()).isPresent()) {
                order.setCustomerName(userService.findUserById(order.getCustomerId()).get().getName());
            }
            if (userService.findUserById(order.getApprovedUser()).isPresent()) {
                order.setApprovedUserName(userService.findUserById(order.getApprovedUser()).get().getName());
            }
        }
        ICsvBeanWriter csvWriter = new CsvBeanWriter(response.getWriter(), CsvPreference.STANDARD_PREFERENCE);
        String[] headers = { "Order #", "Customer ID", "Customer Name", "Approver ID", "Approver Name", "Total Price", "Rating" };
        String[] fieldMapping = { "id", "customerId", "customerName", "approvedUser", "approvedUserName", "totalPrice", "rating" };
        csvWriter.writeHeader(headers);
        for (CafeOrders order: cafeOrdersList) {
            csvWriter.write(order, fieldMapping);
        }
        csvWriter.close();
    }

}

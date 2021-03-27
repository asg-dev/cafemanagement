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
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import sun.awt.ModalExclude;

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
        return "reports";
    }

}

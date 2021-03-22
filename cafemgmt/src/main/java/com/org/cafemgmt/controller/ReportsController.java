package com.org.cafemgmt.controller;

import com.org.cafemgmt.model.CafeOrders;
import com.org.cafemgmt.model.CafeUsers;
import com.org.cafemgmt.model.PrincipalUserDetails;
import com.org.cafemgmt.service.CafeOrderService;
import com.org.cafemgmt.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import sun.awt.ModalExclude;

import java.util.List;

@Controller
public class ReportsController {

    @Autowired
    UserService userService;

    @Autowired
    CafeOrderService cafeOrderService;

    @RequestMapping(value = "/reports", method = RequestMethod.GET)
    public String getReports(Authentication authentication, Model model) {
        PrincipalUserDetails currentUser = (PrincipalUserDetails) authentication.getPrincipal();
        String userEmail = currentUser.getUsername();
        CafeUsers cafeUser = userService.findUserByEmail(userEmail);

        List<CafeOrders> cafeOrdersList = cafeOrderService.listAllOrders();
        cafeOrderService.setMenuItemsInternal(cafeOrdersList);
        model.addAttribute("allOrders", cafeOrdersList);
        return "reports";
    }

}

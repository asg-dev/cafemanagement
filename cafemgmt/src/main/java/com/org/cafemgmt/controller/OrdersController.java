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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@Controller
public class OrdersController {

    @Autowired
    CafeOrderService cafeOrderService;

    @Autowired
    UserService userService;

    @RequestMapping(value = "/pending_orders", method = RequestMethod.GET)
    public String showPendingOrders(Authentication authentication, Model model) {
        PrincipalUserDetails currentUser = (PrincipalUserDetails) authentication.getPrincipal();
        String userEmail = currentUser.getUsername();
        CafeUsers cafeUser = userService.findUserByEmail(userEmail);
        if (cafeUser.getAuthority().equals("ROLE_ADMIN")) {
            model.addAttribute("role", "admin");
        }
        else {
            model.addAttribute("role", "clerk");
        }
        List<CafeOrders> cafeOrdersList = cafeOrderService.getAllPendingOrders();
        cafeOrderService.setMenuItemsInternal(cafeOrdersList);
        model.addAttribute("cafeOrders", cafeOrdersList);
        return "pending_orders";
    }

    @RequestMapping(value = "/orders/{id}/approve", method = RequestMethod.POST)
    public String approveOrder(Authentication authentication, @PathVariable("id") long id) {
        cafeOrderService.approveOrder(id);
        return "redirect:/pending_orders";
    }


    @RequestMapping(value = "/orders/{id}/cancel", method = RequestMethod.POST)
    public String cancelOrder(Authentication authentication, @PathVariable("id") long id) {
        cafeOrderService.cancelOrder(id);
        return "redirect:/pending_orders";
    }

    @RequestMapping(value = "/site/my_orders", method = RequestMethod.GET)
    public String showMyOrders(Authentication authentication, Model model) {
        PrincipalUserDetails currentUser = (PrincipalUserDetails) authentication.getPrincipal();
        String userEmail = currentUser.getUsername();
        CafeUsers cafeUser = userService.findUserByEmail(userEmail);
        long user_id = cafeUser.getId();
        List<CafeOrders> cafeOrdersList = cafeOrderService.getAllOrdersByUserId(user_id);
        cafeOrderService.setMenuItemsInternal(cafeOrdersList);
        model.addAttribute("allOrders", cafeOrdersList);
        model.addAttribute("role","customer");
        return "my_orders";
    }

}

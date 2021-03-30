package com.org.cafemgmt.controller;

import com.org.cafemgmt.common.UserManagement;
import com.org.cafemgmt.model.CafeOrders;
import com.org.cafemgmt.model.CafeRatings;
import com.org.cafemgmt.model.CafeUsers;
import com.org.cafemgmt.model.PrincipalUserDetails;
import com.org.cafemgmt.service.CafeOrderService;
import com.org.cafemgmt.service.CartsService;
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

    @Autowired
    CartsService cartsService;

    @RequestMapping(value = "/pending_orders", method = RequestMethod.GET)
    public String showPendingOrders(Authentication authentication, Model model) {
        String loggedInUsername = UserManagement.getUserName(authentication, userService);
        String authority = UserManagement.getAuthority(authentication, userService);
        if (authority.equals("ROLE_ADMIN")) {
            model.addAttribute("role", "admin");
        } else {
            model.addAttribute("role", "clerk");
        }

        List<CafeOrders> cafeOrdersList = cafeOrderService.getAllPendingOrders();
        cafeOrderService.setMenuItemsInternal(cafeOrdersList);

        model.addAttribute("cafeOrders", cafeOrdersList);
        model.addAttribute("firstCharInUsername", loggedInUsername.charAt(0));
        model.addAttribute("username", loggedInUsername);

        return "pending_orders";
    }

    @RequestMapping(value = "/orders/{id}/approve", method = RequestMethod.POST)
    public String approveOrder(Authentication authentication, @PathVariable("id") long id) {
        long approver_id = userService.findUserByEmail(((PrincipalUserDetails) authentication.getPrincipal()).getUsername()).getId();
        cafeOrderService.approveOrder(id, approver_id);
        return "redirect:/pending_orders?order_updated_successfully";
    }


    @RequestMapping(value = "/orders/{id}/cancel", method = RequestMethod.POST)
    public String cancelOrder(Authentication authentication, @PathVariable("id") long id) {
        long canceller_id = userService.findUserByEmail(((PrincipalUserDetails) authentication.getPrincipal()).getUsername()).getId();
        cafeOrderService.cancelOrder(id, canceller_id);
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

        model.addAttribute("cafeOrders", cafeOrdersList);
        model.addAttribute("role", "customer");

        model.addAttribute("ratings", new CafeRatings());
        return "my_orders";
    }

    @RequestMapping(value = "/orders", method = RequestMethod.GET)
    public String showAllOrders(Authentication authentication, Model model) {
        PrincipalUserDetails currentUser = (PrincipalUserDetails) authentication.getPrincipal();
        String userEmail = currentUser.getUsername();
        CafeUsers cafeUser = userService.findUserByEmail(userEmail);

        if (cafeUser.getAuthority().equals("ROLE_ADMIN")) {
            model.addAttribute("role", "admin");
        } else {
            model.addAttribute("role", "clerk");
        }

        long user_id = cafeUser.getId();
        List<CafeOrders> cafeOrdersList = cafeOrderService.getAllOrders();
        cafeOrderService.setMenuItemsInternal(cafeOrdersList);
        model.addAttribute("firstCharInUsername", cafeUser.getName().charAt(0));
        model.addAttribute("username", cafeUser.getName());
        model.addAttribute("cafeOrders", cafeOrdersList);
        return "orders";
    }

    @RequestMapping(value = "/reports/{id}/generate_invoice", method = RequestMethod.GET)
    public String generateInvoice(@PathVariable("id") long order_id, Model model) {
        CafeOrders cafeOrder = cafeOrderService.getOrderById(order_id);
        if (cafeOrder != null) {
            model.addAttribute("cafeOrder", cafeOrder);
        }
        if (cafeOrder.getStatus() != 2) {
            model.addAttribute("isOrderApproved", false);
        }
        else {
            model.addAttribute("isOrderApproved", true);
        }
        return "invoice";
    }
}

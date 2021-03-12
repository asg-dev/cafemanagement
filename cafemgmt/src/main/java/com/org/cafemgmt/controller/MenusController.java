package com.org.cafemgmt.controller;

import com.org.cafemgmt.model.PrincipalUserDetails;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;

@Controller
public class MenusController {

    @RequestMapping(value = "/site/menus", method = RequestMethod.GET)
    public String showMenus(HttpServletRequest request, Authentication authentication, Model model) {
        // System.out.println(((PrincipalUserDetails) authentication.getPrincipal()).getAuthorities());
        PrincipalUserDetails currentUser = (PrincipalUserDetails) authentication.getPrincipal();
        System.out.println((currentUser.getAuthorities()).getClass());
        System.out.println((currentUser.getAuthorities()));
        if (currentUser.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
            model.addAttribute("role", "admin");
        }
        else if (currentUser.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_CLERK"))) {
            model.addAttribute("role", "clerk");
        }
        else {
            model.addAttribute("role", "customer");
        }
        return "menus";
    }

    // CUSTOMER: MENU (currentpage) - MY ORDERS - CART - LOGOUT
    // CLERK:    MENU - PENDING ORDERS - CART (WALKIN CUSTOMER) - LOGOUT
    // ADMIN:    MENU - PENDING ORDERS - CART (WALKIN CUSTOMER) - ADMIN - LOGOUT

    // ADMIN (in ADMIN page): USERS - MENUS - REPORTS
}

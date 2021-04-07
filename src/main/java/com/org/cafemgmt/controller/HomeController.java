package com.org.cafemgmt.controller;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Controller
public class HomeController {

    // Handles mapping for / page.
    @GetMapping("/")
    public String home() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (!(auth instanceof AnonymousAuthenticationToken)) {
            if (auth.getAuthorities().contains("ROLE_CUSTOMER")) {
                return "redirect:/site/menus";
            }
            else {
                return "redirect:/pending_orders";
            }

        }
        return "login";
    }
}

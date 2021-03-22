package com.org.cafemgmt.controller;

import com.org.cafemgmt.model.CafeUsers;
import com.org.cafemgmt.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

@Controller
@Slf4j
public class RegisterAccountController {
    @Autowired
    UserService userService;

    @RequestMapping(value = "/register_account/{uuid}", method = RequestMethod.GET)
    public String registerUserAccount(@PathVariable UUID uuid, Model model) {
        CafeUsers user = userService.findUserByValidityToken(uuid);
        if (user == null) {
            return "redirect:/login?error";
        }
        log.info(user.getEmailAddress());
        model.addAttribute("userRegistration", user);
        return "register_account";
    }

    @RequestMapping(value = "/register_account", method = RequestMethod.POST)
    public String savePasswordForUser(HttpServletRequest request, @ModelAttribute("userRegistration") CafeUsers cafeUsers) {
        userService.registerUser(cafeUsers);
        return "login";
    }
}

package com.org.cafemgmt.controller;

import com.org.cafemgmt.model.CafeUsers;
import com.org.cafemgmt.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;

@Controller
@Slf4j
public class SignupController {
    @Autowired
    UserService userService;

    @RequestMapping(value="/signup", method= RequestMethod.GET)
    public String register(Model model) {
        log.info("Redirecting to Signup Page!");
        model.addAttribute("userForm", new CafeUsers());
        return "signup";
    }

    @RequestMapping(value="/signup", method=RequestMethod.POST)
    public String signup(HttpServletRequest request, @ModelAttribute("userForm") CafeUsers cafeUsers) {
        System.out.println(cafeUsers.getEmailAddress());
        userService.insertUser(cafeUsers);
        return "login";
    }
}

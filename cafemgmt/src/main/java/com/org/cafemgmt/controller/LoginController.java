package com.org.cafemgmt.controller;

import com.org.cafemgmt.model.CafeUsers;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@Slf4j
public class LoginController {

    @RequestMapping(value="/login", method=RequestMethod.GET)
    public String login(Model model) {
        model.addAttribute("userLoginObj", new CafeUsers());
        return "login";
    }

}

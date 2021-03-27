package com.org.cafemgmt.controller;

import com.org.cafemgmt.common.UserManagement;
import com.org.cafemgmt.model.CafeUsers;
import com.org.cafemgmt.model.PrincipalUserDetails;
import com.org.cafemgmt.service.UserService;
import com.org.cafemgmt.service.UserServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;

@Controller
@Slf4j
public class AdminController {

    @Autowired
    UserService userService;

    @RequestMapping(value = "/admin", method = RequestMethod.GET)
    public String admin(Authentication authentication, Model model) {
        String authority = UserManagement.getAuthority(authentication, userService);

        String loggedInUsername = UserManagement.getUserName(authentication, userService);
        if (authority.equals("ROLE_ADMIN")) {
            model.addAttribute("role", "admin");
        }
        else {
            model.addAttribute("role", "clerk");
        }
        model.addAttribute("firstCharInUsername", loggedInUsername.charAt(0));
        model.addAttribute("username", loggedInUsername);
        return "admin";
    }


}

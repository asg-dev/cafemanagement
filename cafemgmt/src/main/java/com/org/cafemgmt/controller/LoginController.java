package com.org.cafemgmt.controller;

import com.org.cafemgmt.model.CafeUsers;
import com.org.cafemgmt.service.MyUserDetailsService;
import com.org.cafemgmt.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@Slf4j
public class LoginController {

    @Autowired
    UserService userService;
    @Autowired
    MyUserDetailsService myUserDetailsService;

    @RequestMapping(value="/login", method=RequestMethod.GET)
    public String login(Model model) {
        log.info("Inside Login Controller!");
        model.addAttribute("userLoginObj", new CafeUsers());
        // userService.listAllUserDetails();
        return "login";
    }

//    @RequestMapping(value="/login", method=RequestMethod.POST)
//    public String handleLogin(@ModelAttribute("userLoginObj") CafeUsers cafeUsers) throws UsernameNotFoundException {
//        UserDetails user = myUserDetailsService.loadUserByUsername(cafeUsers.getEmailAddress());
//        // System.out.println(user.getName());
//        return "redirect:/menus";
//    }
}

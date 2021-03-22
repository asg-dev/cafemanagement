package com.org.cafemgmt.controller;

import com.org.cafemgmt.model.CafeUsers;
import com.org.cafemgmt.model.PrincipalUserDetails;
import com.org.cafemgmt.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

@Controller
@Slf4j
public class UsersController {
    @Autowired
    UserService userService;

    @RequestMapping(value = "/users", method = RequestMethod.GET)
    private String listAllUsers(HttpServletRequest request, Authentication authentication, Model model) {
        List<CafeUsers> userList = userService.listAllUserDetails();
        model.addAllAttributes(userList);
        PrincipalUserDetails currentUser = (PrincipalUserDetails) authentication.getPrincipal();
        String userEmail = currentUser.getUsername();
        System.out.println(userEmail);
        CafeUsers cafeUser = userService.findUserByEmail(userEmail);
        System.out.println(cafeUser.getName());
        // System.out.println(userService.findUserByEmail(userEmail));
        model.addAttribute("firstCharInUsername", cafeUser.getName().charAt(0));
        model.addAttribute("username", cafeUser.getName());
        model.addAttribute("users", userService.listAllUserDetails());
        return "users";
    }

    @RequestMapping(value = "/users/{id}/edit", method = RequestMethod.GET)
    private String editUser(HttpServletRequest request, Model model, Authentication authentication, @PathVariable Long id) {
        if (userService.findUserById(id).isPresent()) {
            model.addAttribute("userInfo", userService.findUserById(id).get());
        }
        else {
            model.addAttribute("userInfo", "");
        }
        return "edit";
    }

    @RequestMapping(value = "/users/{id}", method = RequestMethod.POST)
    private String updateUsers(HttpServletRequest request, @ModelAttribute("userInfo") CafeUsers user, @PathVariable long id) {
        log.info("Trying to POST for cafeUser. Id: ", id);
        userService.updateInternalUser(user);
        return "redirect:/users";
    }

    @RequestMapping(value = "/users/new", method = RequestMethod.GET)
    private String addNewUser(HttpServletRequest request, Model model, Authentication authentication) {
        PrincipalUserDetails currentUser = (PrincipalUserDetails) authentication.getPrincipal();
        String userEmail = currentUser.getUsername();
        System.out.println(userEmail);
        CafeUsers cafeUser = userService.findUserByEmail(userEmail);
        System.out.println(cafeUser.getName());
        // System.out.println(userService.findUserByEmail(userEmail));
        model.addAttribute("firstCharInUsername", cafeUser.getName().charAt(0));
        model.addAttribute("username", cafeUser.getName());
        model.addAttribute("userForm", new CafeUsers());
        return "new_agent";
    }

    @RequestMapping(value = "/users", method = RequestMethod.POST)
    private String createUser(HttpServletRequest request, @ModelAttribute("userForm") CafeUsers cafeUsers, Authentication authentication) throws MessagingException {
        userService.insertCustomUser(cafeUsers);
        return "redirect:/users";
    }
}
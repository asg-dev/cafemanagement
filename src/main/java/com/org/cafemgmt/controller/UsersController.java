package com.org.cafemgmt.controller;

import com.org.cafemgmt.common.UserManagement;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@Slf4j
public class UsersController {
    @Autowired
    UserService userService;

    @RequestMapping(value = "/users", method = RequestMethod.GET)
    private String listAllUsers(Authentication authentication, Model model) {
        List<CafeUsers> userList = userService.listAllUserDetails();

        String loggedInUsername = UserManagement.getUserName(authentication, userService);

        model.addAttribute("firstCharInUsername", loggedInUsername.charAt(0));
        model.addAttribute("username", loggedInUsername);
        List<CafeUsers> modifiedUserList = new ArrayList<CafeUsers>();
        for (CafeUsers user : userList) {
            if (user.getEmailAddress() != "walkincustomerdefault@freshbrew.com") {
                modifiedUserList.add(user);
            }
        }
        model.addAttribute("users", modifiedUserList);
        return "users";
    }

    @RequestMapping(value = "/users/{id}/edit", method = RequestMethod.GET)
    private String editUser(Model model, Authentication authentication, @PathVariable Long id) {
        Optional<CafeUsers> cafeUser = userService.findUserById(id);
        if (cafeUser.isPresent()) {
            if (cafeUser.get().getAuthority().equals("ROLE_CUSTOMER") ){
                return "redirect:/users?invalid_action";
            }
            model.addAttribute("userInfo", cafeUser.get());
        } else {
            model.addAttribute("userInfo", "");
        }

        String loggedInUsername = UserManagement.getUserName(authentication, userService);

        model.addAttribute("firstCharInUsername", loggedInUsername.charAt(0));
        model.addAttribute("username", loggedInUsername);

        return "edit";
    }

    @RequestMapping(value = "/users/{id}", method = RequestMethod.POST)
    private String updateUsers(@ModelAttribute("userInfo") CafeUsers user, @PathVariable long id, Authentication authentication) {
        log.info("Editing User with Id: " + id + " auth " + user.getAuthority());
        log.info("Logged in User id" + userService.findUserByEmail(((PrincipalUserDetails) authentication.getPrincipal()).getUsername()).getId());
        CafeUsers loggedInUser = userService.findUserByEmail(((PrincipalUserDetails) authentication.getPrincipal()).getUsername());
        if (id == loggedInUser.getId()
                && user.getAuthority().equals("ROLE_CLERK")) {
            return "redirect:/users?update_not_allowed";
        }
        if (loggedInUser.getAuthority().equals("ROLE_CUSTOMER")) {
            return "redirect:/users?update_not_allowed";
        }
        userService.saveCafeUser(user, "UPDATE");
        return "redirect:/users";
    }

    @RequestMapping(value = "/users/new", method = RequestMethod.GET)
    private String addNewUser(Model model, Authentication authentication) {
        String loggedInUsername = UserManagement.getUserName(authentication, userService);

        model.addAttribute("firstCharInUsername", loggedInUsername.charAt(0));
        model.addAttribute("username", loggedInUsername);
        model.addAttribute("userForm", new CafeUsers());
        return "new_agent";
    }

    @RequestMapping(value = "/users", method = RequestMethod.POST)
    private String createUser(@ModelAttribute("userForm") CafeUsers cafeUsers, Authentication authentication) throws MessagingException {
        CafeUsers existingUser = userService.findUserByEmail(cafeUsers.getEmailAddress());
        if (existingUser != null) {
            return "redirect:/users/new?error=user_already_exists";
        }
        userService.saveCafeUser(cafeUsers, "CREATE");
        return "redirect:/users";
    }
}
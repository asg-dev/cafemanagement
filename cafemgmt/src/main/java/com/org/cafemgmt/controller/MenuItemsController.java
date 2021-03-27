package com.org.cafemgmt.controller;

import com.org.cafemgmt.common.UserManagement;
import com.org.cafemgmt.model.CafeMenuItems;
import com.org.cafemgmt.model.CafeMenus;
import com.org.cafemgmt.model.CafeUsers;
import com.org.cafemgmt.model.PrincipalUserDetails;
import com.org.cafemgmt.service.MenuItemsService;
import com.org.cafemgmt.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

@Controller
public class MenuItemsController {
    @Autowired
    MenuItemsService menuItemsService;

    @Autowired
    UserService userService;

    @RequestMapping(value = "/menus/items", method = RequestMethod.POST)
    public String addMenuItem(HttpServletRequest request, Authentication authentication, Model model, @ModelAttribute("menuItemForm") CafeMenuItems cafeMenuItems,
                              @ModelAttribute("menuForm") CafeMenus cafeMenus, @RequestParam("file") MultipartFile file) {
        String loggedInUsername = UserManagement.getUserName(authentication, userService);

        model.addAttribute("firstCharInUsername", loggedInUsername.charAt(0));
        model.addAttribute("username", loggedInUsername);

        // Using the referrer to identify from which page the edit was made - this way, we can redirect them to the same page with the
        // correct information after item creation.
        String referrer = request.getHeader("referer");
        String toReturn = "redirect:/" + referrer.replace("http://localhost:8080/", "");
        String imagePath = menuItemsService.saveMenuItemImage(file);
        if (imagePath == null) {
            return toReturn + "?error=invalid_image_path";
        }
        cafeMenuItems.setImagePath(imagePath);
        menuItemsService.saveMenuItem(cafeMenuItems);

        model.addAttribute("menuForm", new CafeMenus());
        model.addAttribute("menuItemForm", new CafeMenuItems());
        model.addAttribute("menuItems", menuItemsService.getAllMenuItems());

        return toReturn;
    }

    @RequestMapping(value = "/menus/items/new", method = RequestMethod.GET)
    public String addNewMenuItem(Authentication authentication, Model model) {
        String loggedInUsername = UserManagement.getUserName(authentication, userService);

        model.addAttribute("firstCharInUsername", loggedInUsername.charAt(0));
        model.addAttribute("username", loggedInUsername);

        model.addAttribute("menuItemForm", new CafeMenuItems());
        return "new_menu_item";
    }

    @RequestMapping(value = "menus/items", method = RequestMethod.GET)
    public String manageMenus(Authentication authentication, Model model) {
        String loggedInUsername = UserManagement.getUserName(authentication, userService);

        model.addAttribute("firstCharInUsername", loggedInUsername.charAt(0));
        model.addAttribute("username", loggedInUsername);

        model.addAttribute("menuItems", menuItemsService.getAllMenuItems());
        return "menu_items";
    }

    @RequestMapping(value = "menus/items/{id}/delete", method = RequestMethod.POST)
    public String deleteMenuItem(@PathVariable("id") long id) {
        menuItemsService.deleteMenuItem(id);
        return "redirect:/menus/items";
    }
}

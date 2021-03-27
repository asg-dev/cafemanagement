package com.org.cafemgmt.controller;

import com.fasterxml.jackson.databind.util.TypeKey;
import com.org.cafemgmt.common.UserManagement;
import com.org.cafemgmt.model.*;
import com.org.cafemgmt.service.CartsService;
import com.org.cafemgmt.service.MenuItemsService;
import com.org.cafemgmt.service.MenuService;
import com.org.cafemgmt.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Controller
public class MenusController {

    @Autowired
    UserService userService;

    @Autowired
    MenuService menuService;

    @Autowired
    MenuItemsService menuItemsService;

    @Autowired
    CartsService cartsService;

    @RequestMapping(value = "/site/menus", method = RequestMethod.GET)
    public String showMenus(HttpServletRequest request, Authentication authentication, Model model) {
        // System.out.println(((PrincipalUserDetails) authentication.getPrincipal()).getAuthorities());
        PrincipalUserDetails currentUser = (PrincipalUserDetails) authentication.getPrincipal();

        if (currentUser.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
            model.addAttribute("role", "admin");
        }
        else if (currentUser.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_CLERK"))) {
            model.addAttribute("role", "clerk");
        }
        else {
            model.addAttribute("role", "customer");
        }
        CafeUsers loggedInUser = userService.findUserByEmail(currentUser.getUsername());

        model.addAttribute("menus", menuService.listAllActiveMenus());
        model.addAttribute("allMenuItems", menuItemsService.getAllMenuItems());

        CafeCarts cafeCart = cartsService.getCartByUserId(loggedInUser.getId());
        if (cafeCart != null) {
            model.addAttribute("cartItems", cafeCart.getCartItems());
        }
        else {
            model.addAttribute("cartItems", "");
        }

        return "site_menus2";
    }

    // CUSTOMER: MENU (currentpage) - MY ORDERS - CART - LOGOUT
    // CLERK:    MENU - PENDING ORDERS - CART (WALKIN CUSTOMER) - LOGOUT
    // ADMIN:    MENU - PENDING ORDERS - CART (WALKIN CUSTOMER) - ADMIN - LOGOUT

    // ADMIN (in ADMIN page): USERS - MENUS - REPORTS

    @RequestMapping(value = "/menus", method = RequestMethod.GET)
    public String showMenus(Authentication authentication, Model model) {
        String loggedInUsername = UserManagement.getUserName(authentication, userService);

        model.addAttribute("firstCharInUsername", loggedInUsername.charAt(0));
        model.addAttribute("username",loggedInUsername);

        List<CafeMenus> menuList = menuService.listAllMenus();

        for (CafeMenus menu : menuList) {
            List<CafeMenuItems> cafeMenuItems = menuItemsService.findAllMenuItemsById(menu.getMenu_items());
            List<String> menuItemNames = new ArrayList<String>();
            for (CafeMenuItems cafeMenuItem : cafeMenuItems) {
                menuItemNames.add(cafeMenuItem.getName());
            }
            menu.setMenu_item_names(menuItemNames);
        }

        model.addAttribute("menus", menuList);

        return "menus";
    }

    @RequestMapping(value = "/menus/{id}/activate", method = RequestMethod.POST)
    public String activateMenu(@PathVariable long id) {
        menuService.activateMenu(id);
        return "redirect:/menus";
    }

    @RequestMapping(value = "/menus/{id}/deactivate", method = RequestMethod.POST)
    public String deactivateMenu(@PathVariable long id) {
        menuService.deactivateMenu(id);
        return "redirect:/menus";
    }

    @RequestMapping(value = "/menus/new", method = RequestMethod.GET)
    public String addNewMenu(Authentication authentication, Model model) {
        String loggedInUsername = UserManagement.getUserName(authentication, userService);

        model.addAttribute("firstCharInUsername", loggedInUsername.charAt(0));
        model.addAttribute("username", loggedInUsername);

        model.addAttribute("menuItems", menuItemsService.getAllMenuItems());
        model.addAttribute("menuForm", new CafeMenus());
        model.addAttribute("menuItemForm", new CafeMenuItems());

        return "new_menu";
    }

    @RequestMapping(value = "/menus", method = RequestMethod.POST)
    public String createNewMenu(@ModelAttribute("menuForm") CafeMenus cafeMenus) {
        menuService.saveMenu(cafeMenus);
        return "redirect:/menus";
    }

    @RequestMapping(value = "/menus/{id}/edit", method = RequestMethod.GET)
    public String editMenu(@PathVariable long id, Model model) {
        CafeMenus menu = menuService.getMenuById(id).get();

        List<CafeMenuItems> cafeMenuItems = menuItemsService.findAllMenuItemsById(menu.getMenu_items());
        List<String> menuItemNames = new ArrayList<String>();
        for (CafeMenuItems cafeMenuItem : cafeMenuItems) {
            menuItemNames.add(cafeMenuItem.getName());
        }

        menu.setMenu_item_names(menuItemNames);

        model.addAttribute("menuForm", menu);
        model.addAttribute("menuItemForm", new CafeMenuItems());
        model.addAttribute("allMenuItems", menuItemsService.getAllMenuItems());

        return "edit_menu";
    }

    @RequestMapping(value = "/menus/{id}", method = RequestMethod.POST)
    public String updateMenu(@PathVariable long id, @ModelAttribute("menuForm") CafeMenus cafeMenus) {
        menuService.saveMenu(cafeMenus);
        return "redirect:/menus";
    }

    @RequestMapping(value = "/menus/{id}/delete", method = RequestMethod.POST)
    public String deleteMenu(@PathVariable long id) {
        menuService.deleteMenuById(id);
        return "redirect:/menus";
    }
}

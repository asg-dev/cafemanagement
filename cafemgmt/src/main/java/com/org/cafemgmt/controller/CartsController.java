package com.org.cafemgmt.controller;

import com.org.cafemgmt.model.CafeCarts;
import com.org.cafemgmt.model.CafeOrders;
import com.org.cafemgmt.model.CafeUsers;
import com.org.cafemgmt.model.PrincipalUserDetails;
import com.org.cafemgmt.service.CafeOrderService;
import com.org.cafemgmt.service.CartsService;
import com.org.cafemgmt.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Date;
import java.util.Optional;

@Controller
public class CartsController {
    @Autowired
    CartsService cartsService;

    @Autowired
    UserService userService;

    @Autowired
    CafeOrderService cafeOrderService;

    @RequestMapping(value = "/site/menus/cart", method = RequestMethod.GET)
    public String showCart(Authentication authentication, Model model) {
        PrincipalUserDetails currentUser = (PrincipalUserDetails) authentication.getPrincipal();
        System.out.println((currentUser.getAuthorities()).getClass());
        System.out.println((currentUser.getAuthorities()));
        if (currentUser.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
            model.addAttribute("role", "admin");
        } else if (currentUser.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_CLERK"))) {
            model.addAttribute("role", "clerk");
        } else {
            model.addAttribute("role", "customer");
        }
        String userEmail = currentUser.getUsername();
        CafeUsers cafeUser = userService.findUserByEmail(userEmail);

        long user_id = cafeUser.getId();
        cartsService.setCartItems(cartsService.getCartByUserId(user_id));
        model.addAttribute("cafeCart", cartsService.getCartByUserId(user_id));
        return "cart";
    }

    @RequestMapping(value = "/checkout", method = RequestMethod.POST)
    public String placeOrder(Authentication authentication, @ModelAttribute("cafeCart") CafeCarts cafeCart, Model model) {
        cafeOrderService.transformAndSaveCart(cafeCart);

        long deletableCartId = cartsService.getCartByUserId(cafeCart.getUserId()).getId();
        cartsService.deletePlacedCart(deletableCartId);

        return "redirect:/site/menus";
    }
}

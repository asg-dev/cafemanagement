package com.org.cafemgmt.controller;

import com.org.cafemgmt.model.CafeCarts;
import com.org.cafemgmt.model.CafeUsers;
import com.org.cafemgmt.model.PrincipalUserDetails;
import com.org.cafemgmt.service.CartsService;
import com.org.cafemgmt.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
public class RestCartsController {

    @Autowired
    CartsService cartsService;

    @Autowired
    UserService userService;

    @RequestMapping(value = "/api/carts", method = RequestMethod.POST)
    public CafeCarts addToCart(Authentication authentication, @RequestParam("item_id") long id, @RequestParam("quantity") long quantity,
                               @RequestParam("action") String action, @RequestParam("menu_id") long menu_id) {
        System.out.println(id + quantity + action);
        CafeCarts cafeCart = new CafeCarts();

        PrincipalUserDetails currentUser = (PrincipalUserDetails) authentication.getPrincipal();
        String userEmail = currentUser.getUsername();
        CafeUsers cafeUser = userService.findUserByEmail(userEmail);

        long user_id = cafeUser.getId();

        cafeCart.setUserId(user_id);
        // double subtotalPrice = cartsService.calculateSubtotal(id, quantity);
        cafeCart.setCartItems(cartsService.handleCartItems(id, quantity, action, user_id, menu_id));
        cafeCart.setTotalPrice(cartsService.calculateTotalPrice(cafeCart.getCartItems()));
        return cartsService.saveCart(cafeCart);
    }
}

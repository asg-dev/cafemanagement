package com.org.cafemgmt.rest;

import com.org.cafemgmt.model.CafeCarts;
import com.org.cafemgmt.model.CafeUsers;
import com.org.cafemgmt.model.PrincipalUserDetails;
import com.org.cafemgmt.service.CafeOrderService;
import com.org.cafemgmt.service.CartsService;
import com.org.cafemgmt.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
public class RestCartsController {

    @Autowired
    CartsService cartsService;

    @Autowired
    UserService userService;

    @Autowired
    CafeOrderService cafeOrderService;

    @RequestMapping(value = "/api/carts", method = RequestMethod.POST)
    public CafeCarts addToCart(Authentication authentication, @RequestParam(required = false, value = "item_id") Long id, @RequestParam("quantity") Long quantity,
                               @RequestParam("action") String action, @RequestParam("menu_id") Long menu_id) {

        System.out.println(id + quantity + action);
        CafeCarts cafeCart = new CafeCarts();

        CafeUsers cafeUser = userService.findUserByEmail(((PrincipalUserDetails) authentication.getPrincipal()).getUsername());

        long user_id = cafeUser.getId();
        cafeCart.setUserId(user_id);

        cafeCart.setCartItems(cartsService.handleCartItems(id, quantity, action, user_id, menu_id));
        cafeCart.setTotalPrice(cartsService.calculateTotalPrice(cafeCart.getCartItems()));
        CafeCarts savedCart = cartsService.saveCart(cafeCart);
        savedCart.setTotalQuantity(cartsService.getCartByUserId(user_id).getCartItems().split(",").length);

        return savedCart;
    }

}

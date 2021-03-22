package com.org.cafemgmt.service;

import com.org.cafemgmt.model.CafeCarts;

import java.util.Optional;

public interface CartsService {
    public String handleCartItems(long id, long quantity, String action, long user_id, long menu_id);
    // public double calculateSubtotal(long id, long quantity);
    public double calculateTotalPrice(String cartItems);
    public CafeCarts saveCart(CafeCarts cafeCart);
    public CafeCarts getCartByUserId(long user_id);
    public CafeCarts setCartItems(CafeCarts cafeCart);
    public void deletePlacedCart(long id);
}

package com.org.cafemgmt.service;

import com.org.cafemgmt.model.CafeCarts;
import com.org.cafemgmt.model.CafeMenuItems;
import com.org.cafemgmt.model.CartItem;
import com.org.cafemgmt.repository.CafeCartsRepository;
import com.org.cafemgmt.repository.MenuItemsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.util.*;

@Service
public class CartsServiceImpl implements CartsService {
    final String sep = ":";
    @Autowired
    CafeCartsRepository cafeCartsRepository;

    @Autowired
    MenuItemsRepository menuItemsRepository;

    @Override
    public String handleCartItems(long id, long quantity, String action, long user_id, long menu_id) {
        Optional<CafeCarts> cafeCart = cafeCartsRepository.checkCartItemForUser(user_id);
        Optional<CafeMenuItems> cafeMenuItem = menuItemsRepository.findById(id);
        DecimalFormat decimalFormat = new DecimalFormat("##.00");
        double originalPrice = cafeMenuItem.get().getPrice();
        if (cafeCart.isPresent()) {
            // get the cartItems string with format itemId:quantity:subTotal:menuId
            String cartItems = cafeCart.get().getCartItems();
            String[] cartItemArray = cartItems.split(",");
            List<String> list = new ArrayList<String>(Arrays.asList(cartItemArray)); // List of cart items
            for (int i = 0; i < list.size(); i++) {
                if (Arrays.asList(list.get(i).split(":")).get(0).equals(String.valueOf(id))
                        && Arrays.asList(list.get(i).split(":")).get(3).equals(String.valueOf(menu_id))) {
                    long existingQuantity = Long.parseLong(Arrays.asList(list.get(i).split(":")).get(1));
                    double existingSubtotal = Double.parseDouble(Arrays.asList(list.get(i).split(":")).get(2));
                    long quantityToAdd = quantity;
                    double subtotalToAdd = (originalPrice * quantityToAdd);

                    list.set(i, id + ":" + quantityToAdd + ":" + Double.parseDouble(decimalFormat.format(subtotalToAdd)) + ":" + menu_id);
                    break;
                } else {
                    if (i == list.size() - 1) {
                        double subtotalToAdd = (originalPrice * quantity);
                        list.add(id + ":" + quantity + ":" + Double.parseDouble(decimalFormat.format(subtotalToAdd)) + ":" + menu_id);
                        break;
                    }
                }
            }
            return (String.join(",", list));
        }
        double subtotalToAdd = (originalPrice * quantity);
        String cartItem = id + ":" + quantity + ":" + Double.parseDouble(decimalFormat.format(subtotalToAdd)) + ":" + menu_id;
        return cartItem;
    }

    // Calculating subtotal inside handleCartItems method.

    @Override
    public double calculateTotalPrice(String cartItems) {
        String[] cartItemArray = cartItems.split(",");
        double totalPrice = 0;

        for (String cartItem : cartItemArray) {
            totalPrice += Double.parseDouble((cartItem.split(":"))[2]);
        }
        DecimalFormat decimalFormat = new DecimalFormat("##.00");
        return Double.parseDouble(decimalFormat.format(totalPrice));
    }

    @Override
    public CafeCarts saveCart(CafeCarts cafeCart) {
        Optional<CafeCarts> existingCart = cafeCartsRepository.checkCartItemForUser(cafeCart.getUserId());
        if (existingCart.isPresent()) {
            CafeCarts existingCafeCart = existingCart.get();
            existingCafeCart.setUpdated_at(new Date());
            existingCafeCart.setCartItems(cafeCart.getCartItems());
            existingCafeCart.setTotalPrice(cafeCart.getTotalPrice());
            return cafeCartsRepository.save(existingCafeCart);
        }
        cafeCart.setCreated_at(new Date());
        cafeCart.setUpdated_at(new Date());
        return cafeCartsRepository.save(cafeCart);
    }

    @Override
    public CafeCarts getCartByUserId(long user_id) {
        if (cafeCartsRepository.checkCartItemForUser(user_id).isPresent()) {
            return cafeCartsRepository.checkCartItemForUser(user_id).get();
        }
        return null;
    }

    @Override
    public CafeCarts setCartItems(CafeCarts cafeCart) {
        if (cafeCart != null) {
            String cartItems = cafeCart.getCartItems();
            String[] cartItemArray = cartItems.split(",");
            List<CartItem> cartItemList = new ArrayList<CartItem>();
            for (int i = 0; i < cartItemArray.length; i++) {
                CartItem cartItem = new CartItem();
                String[] cart = cartItemArray[i].split(":");
                cartItem.setMenuItem(Long.parseLong(cart[0]));
                cartItem.setMenuItemName(menuItemsRepository.findById(cartItem.getMenuItem()).get().getName());
                cartItem.setQuantity(Long.parseLong(cart[1]));
                cartItem.setSubTotal(Double.parseDouble(cart[2]));
                cartItemList.add(cartItem);
            }
            cafeCart.setCartItemsInternal(cartItemList);
            return cafeCart;
        } else {
            return null;
        }
    }

    @Override
    public void deletePlacedCart(long id) {
        cafeCartsRepository.deleteById(id);
    }


}

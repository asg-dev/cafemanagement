package com.org.cafemgmt.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CartItem {
    private long menuItem;
    private String menuItemName;
    private String menuItemDescription;
    private String imagePath;
    private double individualPrice;
    private long quantity;
    private double subTotal;
}

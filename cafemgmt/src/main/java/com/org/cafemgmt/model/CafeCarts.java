package com.org.cafemgmt.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Entity
@Getter
@Setter
public class CafeCarts {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;
    @Column(unique = true)
    private long userId;
    private String cartItems;
    private double totalPrice;
    private Date created_at;
    private Date updated_at;
    @Transient
    private List<CartItem> cartItemsInternal;
    @Transient
    private long totalQuantity;
}

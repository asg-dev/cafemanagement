package com.org.cafemgmt.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
public class CafeOrders {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;
    private long customerId;
    @Transient
    private String customerName;
    private String cartItemList;
    @Transient
    private List<CartItem> cartItemListInternal;
    private long approvedUser;
    private long status; // 1 - PENDING, 2 - APPROVED, 3 - CANCELLED
    private Date createdAt;
    private Date updatedAt;
    private double totalPrice;
    private long rating;
}

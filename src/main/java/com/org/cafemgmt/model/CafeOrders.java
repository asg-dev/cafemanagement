package com.org.cafemgmt.model;

import lombok.*;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
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
    @Transient
    private String customerEmail;
    @Transient
    private String formattedOrderDate;
    private String cartItemList;
    @Transient
    private List<CartItem> cartItemListInternal;
    private long approvedUser;
    @Transient
    private String approvedUserName;
    private long status; // 1 - PENDING, 2 - APPROVED, 3 - CANCELLED
    private Date createdAt;
    private Date updatedAt;
    private double totalPrice;
    private long rating;
}

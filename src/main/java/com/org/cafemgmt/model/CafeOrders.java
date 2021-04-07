package com.org.cafemgmt.model;

import com.fasterxml.jackson.annotation.JsonView;
import com.org.cafemgmt.views.CafeOrdersView;
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
    @JsonView(CafeOrdersView.ViewToReturnOrders.class)
    private long id;
    @JsonView(CafeOrdersView.ViewToReturnOrders.class)
    private long customerId;
    @Transient
    private String customerName;
    @Transient
    private String customerEmail;
    @Transient
    private String formattedOrderDate;
    @JsonView(CafeOrdersView.ViewToReturnOrders.class)
    private String cartItemList;
    @Transient
    private List<CartItem> cartItemListInternal;
    @JsonView(CafeOrdersView.ViewToReturnOrders.class)
    private long approvedUser;
    @Transient
    private String approvedUserName;
    @JsonView(CafeOrdersView.ViewToReturnOrders.class)
    private long status; // 1 - PENDING, 2 - APPROVED, 3 - CANCELLED
    @JsonView(CafeOrdersView.ViewToReturnOrders.class)
    private Date createdAt;
    @JsonView(CafeOrdersView.ViewToReturnOrders.class)
    private Date updatedAt;
    @JsonView(CafeOrdersView.ViewToReturnOrders.class)
    private double totalPrice;
    @JsonView(CafeOrdersView.ViewToReturnOrders.class)
    private long rating;
}

package com.org.cafemgmt.service;

import com.org.cafemgmt.model.CafeCarts;
import com.org.cafemgmt.model.CafeOrders;

import java.util.List;
import java.util.Optional;

public interface CafeOrderService {
    public CafeOrders saveCafeOrder(CafeOrders cafeOrder);

    public List<CafeOrders> getAllPendingOrders();

    public List<CafeOrders> getAllApprovedOrders();

    public List<CafeOrders> listAllOrders();

    public List<CafeOrders> setMenuItemsInternal(List<CafeOrders> cafeOrdersList);

    public void approveOrder(long id, long approver_id);

    public void cancelOrder(long id, long canceller_id);

    public List<CafeOrders> getAllOrdersByUserId(long user_id);

    public CafeOrders getOrderById(long order_id);

    public List<CafeOrders> getAllOrders();

    public CafeOrders transformAndSaveCart(CafeCarts cafeCart);

    public List<CafeOrders> getOrdersForReport(String dateRange, long customerId, long approverId);

    public double getSaleTotalForReport(List<CafeOrders> obtainedOrders);

    public long getTotalRatingsForReport(List<CafeOrders> obtainedOrders);

    public List<CafeOrders> getAllCancelledOrders();
}
package com.org.cafemgmt.service;

import com.org.cafemgmt.model.CafeOrders;

import java.util.List;
import java.util.Optional;

public interface CafeOrderService {
    public void saveCafeOrder(CafeOrders cafeOrder);
    public List<CafeOrders> getAllPendingOrders();
    public List<CafeOrders> listAllOrders();
    public List<CafeOrders> setMenuItemsInternal(List<CafeOrders> cafeOrdersList);
    public void approveOrder(long id);
    public void cancelOrder(long id);
    public List<CafeOrders> getAllOrdersByUserId(long user_id);
}

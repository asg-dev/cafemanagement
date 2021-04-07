package com.org.cafemgmt.service;

import com.org.cafemgmt.exceptionhandlers.CafeInvalidParameterException;
import com.org.cafemgmt.model.*;
import com.org.cafemgmt.repository.CafeCartsRepository;
import com.org.cafemgmt.repository.CafeOrderRepository;
import com.org.cafemgmt.repository.MenuItemsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class CafeOrderServiceImpl implements CafeOrderService {

    @Autowired
    CafeOrderRepository cafeOrderRepository;

    @Autowired
    MenuItemsRepository menuItemsRepository;

    @Autowired
    CafeCartsRepository cafeCartsRepository;

    @Autowired
    UserService userService;

    @Override
    @Transactional
    public CafeOrders saveCafeOrder(CafeOrders cafeOrder) {
        if(cafeOrder.getCreatedAt() == null) {
            cafeOrder.setCreatedAt(new Date());
        }
        cafeOrder.setUpdatedAt(new Date());
        return cafeOrderRepository.save(cafeOrder);
    }

    @Override
    public List<CafeOrders> getAllPendingOrders() {
        return cafeOrderRepository.listAllPendingOrders();
    }

    @Override
    public List<CafeOrders> getAllApprovedOrders() {
        return cafeOrderRepository.listAllApprovedOrders();
    }

    @Override
    public List<CafeOrders> listAllOrders() {
        return cafeOrderRepository.findAll(Sort.by(Sort.Direction.DESC, "createdAt"));
    }

    @Override
    public List<CafeOrders> setMenuItemsInternal(List<CafeOrders> cafeOrdersList) {

        for (int i = 0; i < cafeOrdersList.size(); i++) {
            String[] cartItemArray = cafeOrdersList.get(i).getCartItemList().split(",");
            List<CartItem> cartItemList = new ArrayList<CartItem>();
            for (int j = 0; j < cartItemArray.length; j++) {
                String[] cartItem = cartItemArray[j].split(":");
                CartItem cartItem1 = new CartItem();
                cartItem1.setMenuItem(Long.parseLong(cartItem[0]));
                cartItem1.setQuantity(Long.parseLong(cartItem[1]));
                cartItem1.setSubTotal(Double.parseDouble(cartItem[2]));
                if (menuItemsRepository.findById(cartItem1.getMenuItem()).isPresent()) {
                    Optional<CafeMenuItems> cafeMenuItems = menuItemsRepository.findById(cartItem1.getMenuItem());
                    if (cafeMenuItems.isPresent()) {
                        cartItem1.setMenuItemName(cafeMenuItems.get().getName());
                        cartItem1.setMenuItemDescription(cafeMenuItems.get().getDescription());
                        cartItem1.setImagePath(cafeMenuItems.get().getImagePath());
                        cartItem1.setIndividualPrice(cafeMenuItems.get().getPrice());
                    }
                }
                cartItemList.add(cartItem1);
            }
            cafeOrdersList.get(i).setCartItemListInternal(cartItemList);
            if (userService.findUserById(cafeOrdersList.get(i).getApprovedUser()).isPresent()) {
                cafeOrdersList.get(i).setApprovedUserName(userService.findUserById(cafeOrdersList.get(i).getApprovedUser()).get().getName());
            }
            if (userService.findUserById(cafeOrdersList.get(i).getCustomerId()).isPresent()) {
                cafeOrdersList.get(i).setCustomerName(userService.findUserById(cafeOrdersList.get(i).getCustomerId()).get().getName());
                cafeOrdersList.get(i).setCustomerEmail(userService.findUserById(cafeOrdersList.get(i).getCustomerId()).get().getEmailAddress());
            }
        }
        return cafeOrdersList;
    }

    @Override
    public void approveOrder(long id, long approver_id) {
        Optional<CafeOrders> cafeOrder = cafeOrderRepository.findById(id);
        if (cafeOrder.isPresent()) {
            CafeOrders cafeOrderAct = cafeOrder.get();
            cafeOrderAct.setApprovedUser(approver_id);
            cafeOrderAct.setStatus(2);
            cafeOrderRepository.save(cafeOrderAct);
        }
    }

    @Override
    public void cancelOrder(long id, long canceller_id) {
        Optional<CafeOrders> cafeOrder = cafeOrderRepository.findById(id);
        if (cafeOrder.isPresent()) {
            CafeOrders cafeOrderAct = cafeOrder.get();
            cafeOrderAct.setApprovedUser(canceller_id);
            cafeOrderAct.setStatus(3);
            cafeOrderRepository.save(cafeOrderAct);
        }
    }

    @Override
    public List<CafeOrders> getAllOrdersByUserId(long user_id) {
        return cafeOrderRepository.findAllByUserId(user_id);
    }

    @Override
    public CafeOrders getOrderById(long order_id) {
        Optional<CafeOrders> cafeOrdersOptional = cafeOrderRepository.findById(order_id);
        if (cafeOrdersOptional.isPresent()) {
            List<CafeOrders> cafeOrdersList = new ArrayList<CafeOrders>();
            cafeOrdersList.add(cafeOrdersOptional.get());
            setMenuItemsInternal(cafeOrdersList);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMMM dd, yyyy");
            String date = simpleDateFormat.format(cafeOrdersList.get(0).getCreatedAt());
            cafeOrdersList.get(0).setFormattedOrderDate(date);
            return cafeOrdersList.get(0);
        }
        return null;
    }

    @Override
    public List<CafeOrders> getAllOrders() {
        return cafeOrderRepository.findAll();
    }

    @Override
    public CafeOrders transformAndSaveCart(CafeCarts cafeCart) {
        CafeOrders cafeOrder = new CafeOrders();
        Optional<CafeUsers> cafeUsersOptional = userService.findUserById(cafeCart.getUserId());
        long user_id = 0;
        if (cafeUsersOptional.isPresent()) {
            CafeUsers loggedInUser = cafeUsersOptional.get();
            if (loggedInUser.isInternalUser()) {
                if (userService.getWalkinCustomerId() != -1) {
                    user_id = userService.getWalkinCustomerId();
                }
                else {
                    user_id = cafeCart.getUserId();
                }
            } else {
                user_id = cafeCart.getUserId();
            }
        }
        cafeOrder.setCustomerId(user_id);
        cafeOrder.setCartItemList(cafeCart.getCartItems());
        cafeOrder.setTotalPrice(cafeCart.getTotalPrice());
        cafeOrder.setStatus(1);
        cafeOrder.setCreatedAt(new Date());
        cafeOrder.setUpdatedAt(new Date());
        return saveCafeOrder(cafeOrder);
    }

    @Override
    public List<CafeOrders> getOrdersForReport(String dateRange, long customerId, long approverId) {
        String[] completeRange = new String[2];
        if (dateRange != null) {
            completeRange = dateRange.split(" - ");
        }
        CafeOrders cafeOrder = CafeOrders.builder().customerId(customerId).approvedUser(approverId).build();
        try {
            if (dateRange != null && !dateRange.trim().isEmpty()&& customerId == 0 && approverId == 0) {
                return cafeOrderRepository.findWithinDateRange(new Date(completeRange[0]), new Date(completeRange[1]));
            } else if (dateRange != null && !dateRange.trim().isEmpty()&& customerId != 0 && approverId == 0) {
                return cafeOrderRepository.findWithinDateRangeCustomerId(new Date(completeRange[0]), new Date(completeRange[1]), customerId);
            } else if (dateRange != null && !dateRange.trim().isEmpty() && customerId == 0 && approverId != 0) {
                return cafeOrderRepository.findWithinDateRangeApproverId(new Date(completeRange[0]), new Date(completeRange[1]), approverId);
            } else if (dateRange != null && !dateRange.trim().isEmpty() && customerId != 0 && approverId != 0) {
                return cafeOrderRepository.findWithinDateRangeBothIds(new Date(completeRange[0]), new Date(completeRange[1]), customerId, approverId);
            } else if (dateRange == null && customerId == 0 && approverId == 0) {
                return cafeOrderRepository.findAll();
            } else if (dateRange == null && customerId != 0 && approverId == 0) {
                return cafeOrderRepository.findByCustomerId(customerId);
            } else if (dateRange == null && customerId == 0 && approverId != 0) {
                return cafeOrderRepository.findByApproverId(approverId);
            } else if (dateRange == null && customerId != 0 && approverId != 0) {
                return cafeOrderRepository.findByCustomerIdAndApproverId(customerId, approverId);
            } else {
                return cafeOrderRepository.findAll(Example.of(cafeOrder));
            }
        }
        catch (IllegalArgumentException exception) {
            throw new CafeInvalidParameterException("Invalid Parameter in Request");
        }
    }

    @Override
    public double getSaleTotalForReport(List<CafeOrders> obtainedOrders) {
        double totalSale = 0;
        for (CafeOrders order : obtainedOrders) {
            totalSale += (order.getTotalPrice());
        }
        return totalSale;
    }

    @Override
    public long getTotalRatingsForReport(List<CafeOrders> obtainedOrders) {
        long totalRating = 0;
        for (CafeOrders order : obtainedOrders) {
            if (order.getRating() != 0) {
                totalRating += 1;
            }
        }
        return totalRating;
    }

    @Override
    public List<CafeOrders> getAllCancelledOrders() {
        return cafeOrderRepository.listAllCancelledOrders();
    }

}

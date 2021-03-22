package com.org.cafemgmt.service;

import com.org.cafemgmt.model.CafeMenuItems;
import com.org.cafemgmt.model.CafeOrderInvoices;
import com.org.cafemgmt.model.CafeOrders;
import com.org.cafemgmt.model.CartItem;
import com.org.cafemgmt.repository.CafeCartsRepository;
import com.org.cafemgmt.repository.CafeInvoiceRepository;
import com.org.cafemgmt.repository.CafeOrderRepository;
import com.org.cafemgmt.repository.MenuItemsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public class CafeOrderServiceImpl implements CafeOrderService {

    @Autowired
    CafeOrderRepository cafeOrderRepository;

    @Autowired
    MenuItemsRepository menuItemsRepository;

    @Autowired
    CafeCartsRepository cafeCartsRepository;

    @Autowired
    CafeInvoiceRepository cafeInvoiceRepository;

    @Autowired
    UserService userService;

    @Override
    @Transactional
    public void saveCafeOrder(CafeOrders cafeOrder) {
        cafeOrderRepository.save(cafeOrder);
    }

    @Override
    public List<CafeOrders> getAllPendingOrders() {
        return cafeOrderRepository.listAllPendingOrders();
    }

    @Override
    public List<CafeOrders> listAllOrders() {
        return cafeOrderRepository.findAll();
    }

    @Override
    public List<CafeOrders> setMenuItemsInternal(List<CafeOrders> cafeOrdersList) {

        for (int i=0; i < cafeOrdersList.size(); i++) {
            String[] cartItemArray = cafeOrdersList.get(i).getCartItemList().split(",");
            List<CartItem> cartItemList = new ArrayList<CartItem>();
            for (int j=0; j < cartItemArray.length; j++){
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
                    }
                }
                cartItemList.add(cartItem1);
            }
            cafeOrdersList.get(i).setCartItemListInternal(cartItemList);
            if (userService.findUserById(cafeOrdersList.get(i).getCustomerId()).isPresent()) {
                cafeOrdersList.get(i).setCustomerName(userService.findUserById(cafeOrdersList.get(i).getCustomerId()).get().getName());
            }
        }
        return cafeOrdersList;
    }

    @Override
    public void approveOrder(long id) {
        Optional<CafeOrders> cafeOrder = cafeOrderRepository.findById(id);
        if (cafeOrder.isPresent()) {
            CafeOrders cafeOrderAct = cafeOrder.get();
            cafeOrderAct.setStatus(2);
            cafeOrderRepository.save(cafeOrderAct);
            CafeOrderInvoices cafeOrderInvoice = new CafeOrderInvoices();
            cafeOrderInvoice.setOrderId(cafeOrderAct.getId());
            cafeOrderInvoice.setCreatedAt(new Date());
            cafeOrderInvoice.setUpdatedAt(new Date());
            cafeInvoiceRepository.save(cafeOrderInvoice);
        }
    }

    @Override
    public void cancelOrder(long id) {
        Optional<CafeOrders> cafeOrder = cafeOrderRepository.findById(id);
        if (cafeOrder.isPresent()) {
            CafeOrders cafeOrderAct = cafeOrder.get();
            cafeOrderAct.setStatus(3);
            cafeOrderRepository.save(cafeOrderAct);
        }
    }

    @Override
    public List<CafeOrders> getAllOrdersByUserId(long user_id) {
        return cafeOrderRepository.findAllByUserId(user_id);
    }
}

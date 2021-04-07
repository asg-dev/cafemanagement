package com.org.cafemgmt.rest;

import com.fasterxml.jackson.annotation.JsonView;
import com.org.cafemgmt.exceptionhandlers.CafeEntityNotFoundException;
import com.org.cafemgmt.exceptionhandlers.CafeInvalidParameterException;
import com.org.cafemgmt.model.CafeOrders;
import com.org.cafemgmt.model.PrincipalUserDetails;
import com.org.cafemgmt.service.*;
import com.org.cafemgmt.views.CafeOrdersView;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
@Slf4j
public class RestOrdersController {

    @Autowired
    CafeOrderService cafeOrderService;

    @Autowired
    MenuItemsService menuItemsService;

    @Autowired
    CartsService cartsService;

    @Autowired
    MenuService menuService;

    @Autowired
    UserService userService;

    @JsonView(CafeOrdersView.ViewToReturnOrders.class)
    @RequestMapping(value = "/api/orders", method = RequestMethod.GET)
    public List<CafeOrders> listAllOrders(@RequestParam(value = "status", required = false) Long status) {
        if (status != null) {
            if (! (status > 0 && status < 4)) {
                throw new CafeInvalidParameterException("Invalid value for 'status' in request");
            }

            List<CafeOrders> cafeOrdersList = new ArrayList<>();

            if (status == 1) {
                cafeOrdersList = cafeOrderService.getAllPendingOrders();
            }
            if (status == 2) {
                cafeOrdersList = cafeOrderService.getAllApprovedOrders();
            }
            if (status == 3) {
                cafeOrdersList = cafeOrderService.getAllCancelledOrders();
            }
            return cafeOrdersList;
        }
        return cafeOrderService.getAllOrders();
    }

    @JsonView(CafeOrdersView.ViewToReturnOrders.class)
    @RequestMapping(value = "/api/orders", method = RequestMethod.POST)
    public ResponseEntity<Object> createOrder(Authentication authentication,
                                              @RequestBody CafeOrders cafeOrder,
                                              @RequestParam(name = "approved", required = false) boolean approved) {
        if (cafeOrder.getId() != 0 || cafeOrder.getCreatedAt() != null || cafeOrder.getUpdatedAt() != null || cafeOrder.getTotalPrice() != 0) {
            throw new CafeInvalidParameterException("Invalid parameter in request: id, created_at, updated_at, totalPrice not allowed in request");
        }
        if (!(cafeOrder.getStatus() > 0 && cafeOrder.getStatus() < 4)) {
            throw new CafeInvalidParameterException("Invalid status value. Accepted values [ 1 - Pending, 2 - Approved, 3 - Cancelled ]");
        }
        if (cafeOrder.getStatus() != 1 && !approved) {
            throw new CafeInvalidParameterException("Invalid Status in Request, only 1 allowed when approved flag is not set.");
        }
        if (cafeOrder.getStatus() == 3 && approved) {
            throw new CafeInvalidParameterException("Invalid Status in Request. Status cannot be 3 when approved flag is set.");
        }
        if (cafeOrder.getCartItemList() == null) {
            throw new CafeInvalidParameterException("Missing Cart Items. Add cart items as a comma-separated list of pipe-separated values." +
                    "like: itemId:quantity:menuId,itemId2:quantity2:menuId2,...");
        }
        if (cafeOrder.getCustomerId() == 0) {
            throw new CafeInvalidParameterException("Missing Customer Id in request");
        }
        validateItemListParameter(cafeOrder.getCartItemList());
        String cartItemList = buildCartItemList(cafeOrder.getCartItemList());
        cafeOrder.setCartItemList(cartItemList);
        if (userService.findUserById(cafeOrder.getCustomerId()).isPresent()) {
            cafeOrder.setCustomerName(userService.findUserById(cafeOrder.getCustomerId()).get().getName());
            cafeOrder.setCustomerEmail(userService.findUserById(cafeOrder.getCustomerId()).get().getEmailAddress());
        }

        if (approved) {
            cafeOrder.setStatus(2);
        } else {
            cafeOrder.setStatus(1);
        }
        cafeOrder.setApprovedUser(userService.findUserByEmail(((PrincipalUserDetails) authentication.getPrincipal()).getUsername()).getId());
        cafeOrder.setTotalPrice(cartsService.calculateTotalPrice(cartItemList));
        return ResponseEntity.status(200).body(cafeOrderService.saveCafeOrder(cafeOrder));
    }

    @JsonView(CafeOrdersView.ViewToReturnOrders.class)
    @RequestMapping(value = "/api/orders/{id}/approve", method = RequestMethod.POST)
    public ResponseEntity<Object> approveOrder(Authentication authentication, @PathVariable("id") long id) {
        CafeOrders cafeOrder = cafeOrderService.getOrderById(id);
        if (cafeOrder == null) {
            throw new CafeEntityNotFoundException("No Order found with id: " + id);
        }
        if (cafeOrder.getStatus() == 3) {
            throw new CafeInvalidParameterException("Order #" + id + " is a cancelled order. Approval allowed only on pending orders.");
        }
        if (cafeOrder.getStatus() == 2) {
            throw new CafeInvalidParameterException("Order #" + id + " has already been approved.");
        }
        cafeOrder.setStatus(2);
        cafeOrder.setApprovedUser(userService.findUserByEmail(((PrincipalUserDetails) authentication.getPrincipal()).getUsername()).getId());
        return ResponseEntity.status(200).body(cafeOrderService.saveCafeOrder(cafeOrder));
    }

    @JsonView(CafeOrdersView.ViewToReturnOrders.class)
    @RequestMapping(value = "/api/orders/approve_all", method = RequestMethod.POST)
    public ResponseEntity<Object> approveAllPendingOrders(Authentication authentication) {
        List<CafeOrders> pendingOrdersList = cafeOrderService.getAllPendingOrders();
        List<CafeOrders> result = new ArrayList<>();
        for (CafeOrders order : pendingOrdersList) {
            if (order.getStatus() == 1) {
                order.setStatus(2);
                order.setApprovedUser(userService.findUserByEmail(((PrincipalUserDetails) authentication.getPrincipal()).getUsername()).getId());
                result.add(cafeOrderService.saveCafeOrder(order));
            }
        }
        return ResponseEntity.status(200).body(result);
    }

    @JsonView(CafeOrdersView.ViewToReturnOrders.class)
    @RequestMapping(value = "/api/orders/cancel_all", method = RequestMethod.POST)
    public ResponseEntity<Object> cancelAllPendingOrders(Authentication authentication) {
        List<CafeOrders> pendingOrdersList = cafeOrderService.getAllPendingOrders();
        List<CafeOrders> result = new ArrayList<>();
        for (CafeOrders order : pendingOrdersList) {
            if (order.getStatus() == 1) {
                order.setStatus(3);
                order.setApprovedUser(userService.findUserByEmail(((PrincipalUserDetails) authentication.getPrincipal()).getUsername()).getId());
                result.add(cafeOrderService.saveCafeOrder(order));
            }
        }
        return ResponseEntity.status(200).body(result);
    }

    @JsonView(CafeOrdersView.ViewToReturnOrders.class)
    @RequestMapping(value = "/api/orders/{id}/cancel", method = RequestMethod.POST)
    public ResponseEntity<Object> cancelOrder(Authentication authentication, @PathVariable("id") long id) {
        CafeOrders cafeOrder = cafeOrderService.getOrderById(id);
        if (cafeOrder == null) {
            throw new CafeEntityNotFoundException("No Order found with id: " + id);
        }
        if (cafeOrder.getStatus() == 2) {
            throw new CafeInvalidParameterException("Order #" + id + " is an approved order. Cancellation allowed only on pending orders.");
        }
        if (cafeOrder.getStatus() == 3) {
            throw new CafeInvalidParameterException("Order #" + id + " has already been cancelled.");
        }
        cafeOrder.setStatus(3);
        cafeOrder.setApprovedUser(userService.findUserByEmail(((PrincipalUserDetails) authentication.getPrincipal()).getUsername()).getId());
        return ResponseEntity.status(200).body(cafeOrderService.saveCafeOrder(cafeOrder));
    }


    private String buildCartItemList(String cartItemList) {

        List<String> build = new ArrayList<>();
        String[] cartItems = cartItemList.split(",");

        for (String item : cartItems) {
            List<String> items = Arrays.asList(item.split(":"));
            double totalPrice = menuItemsService.getMenuItemById(Long.parseLong(items.get(0))).getPrice() * Long.parseLong(items.get(1));
            build.add(items.get(0) + ":" + items.get(1) + ":" + totalPrice + ":" + items.get(2));
        }

        log.info(String.valueOf(build));
        return String.join(",", build);
    }

    private void validateItemListParameter(String cartItemList) {
        String[] cartItems = cartItemList.split(",");
        // Validating Item Id & menu Id
        try {
            for (String order : cartItems) {
                if (order.split(":").length != 3) {
                    throw new CafeInvalidParameterException("Invalid Cart Items list");
                }
                Long itemId = Long.parseLong(order.split(":")[0]);
                Long menuId = Long.parseLong(order.split(":")[2]);
                if (menuItemsService.getMenuItemById(itemId) == null) {
                    throw new CafeInvalidParameterException("Invalid ItemId in Request.");
                }
                if (! menuService.getMenuById(menuId).isPresent()) {
                    throw new CafeInvalidParameterException("Invalid MenuId in Request");
                }
            }
        }
        catch (NumberFormatException | ArrayIndexOutOfBoundsException exception) {
            // NumberFormatException when parsing as Long
            throw new CafeInvalidParameterException("Invalid Cart Items List. " + exception.getMessage());
        }
        catch (Exception exception) { // Catching all exceptions here.
            if (! exception.equals(new CafeInvalidParameterException("Invalid Cart Items list"))) {
                throw new CafeInvalidParameterException("Invalid Cart Items list. " + exception.getMessage());
            }
        }
    }
}

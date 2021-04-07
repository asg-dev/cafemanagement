package com.org.cafemgmt.rest;

import com.fasterxml.jackson.annotation.JsonView;
import com.org.cafemgmt.exceptionhandlers.CafeAuthorizationException;
import com.org.cafemgmt.exceptionhandlers.CafeEntityNotFoundException;
import com.org.cafemgmt.exceptionhandlers.CafeInvalidParameterException;
import com.org.cafemgmt.model.CafeMenus;
import com.org.cafemgmt.service.MenuItemsService;
import com.org.cafemgmt.service.MenuService;
import com.org.cafemgmt.views.CafeMenuView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class RestMenusController {

    @Autowired
    MenuService menuService;

    @Autowired
    MenuItemsService menuItemsService;

    @RequestMapping(value = "/api/menus", method = RequestMethod.GET)
    @JsonView(CafeMenuView.ViewToReturnMenus.class)
    public ResponseEntity<Object> listAllMenus() {
        return ResponseEntity.status(200).body(menuService.listAllMenus());
    }

    @RequestMapping(value = "api/menus/{id}", method = RequestMethod.GET)
    @JsonView(CafeMenuView.ViewToReturnMenus.class)
    public ResponseEntity<Object> getMenu(@PathVariable long id) {
        Optional<CafeMenus> existingMenu = menuService.getMenuById(id);
        if (!existingMenu.isPresent()) {
            throw new CafeEntityNotFoundException("Menu with Id <" + id + "> not found");
        }
        return ResponseEntity.status(200).body(menuService.getMenuById(id));
    }

    @RequestMapping(value = "api/menus", method = RequestMethod.POST)
    @JsonView(CafeMenuView.ViewToReturnMenus.class)
    public ResponseEntity<Object> addNewMenu(@RequestBody CafeMenus cafeMenus) {
        if (cafeMenus.getId() != 0 || cafeMenus.getCreated_at() != null || cafeMenus.getUpdated_at() != null) {
            throw new CafeInvalidParameterException("Invalid Parameter in Request");
        }
        validateInput(cafeMenus);
        return ResponseEntity.status(200).body(menuService.saveMenu(cafeMenus));
    }

    @RequestMapping(value = "api/menus/{id}", method = RequestMethod.PUT)
    @JsonView(CafeMenuView.ViewToReturnMenus.class)
    public ResponseEntity<Object> updateMenu(@PathVariable long id, @RequestBody CafeMenus cafeMenus) {
        Optional<CafeMenus> existingMenu = menuService.getMenuById(id);
        if (!existingMenu.isPresent()) {
            throw new CafeEntityNotFoundException("Menu with Id <" + id + "> not found");
        }
        if (cafeMenus.getId() != 0 || cafeMenus.getCreated_at() != null || cafeMenus.getUpdated_at() != null) {
            throw new CafeInvalidParameterException("Invalid Parameter in Request");
        }
        validateInput(cafeMenus);
        return ResponseEntity.status(200).body(menuService.updateMenu(cafeMenus, id));
    }

    @RequestMapping(value = "api/menus/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Object> deleteMenu(@PathVariable long id) {

        Optional<CafeMenus> existingMenu = menuService.getMenuById(id);
        if (!existingMenu.isPresent()) {
            throw new CafeEntityNotFoundException("Menu with Id <" + id + "> not found");
        }

        menuService.deleteMenuById(id);
        return ResponseEntity.status(204).build();
    }

    public void validateInput(CafeMenus cafeMenus) {
        if (cafeMenus.getName() == null || cafeMenus.getDescription() == null || cafeMenus.getMenu_items() == null || cafeMenus.getMenu_items().size() == 0) {
            throw new CafeInvalidParameterException("Name, Description or Menu Items of Menu cannot be empty");
        }
        List<Long> incomingList = cafeMenus.getMenu_items();
        if (!menuItemsService.checkMenuItemValidity(incomingList)) {
            throw new CafeInvalidParameterException("Items in menu_list are invalid.");
        }
    }
}

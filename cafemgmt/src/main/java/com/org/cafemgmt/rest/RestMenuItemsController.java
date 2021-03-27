package com.org.cafemgmt.rest;

import com.fasterxml.jackson.annotation.JsonView;
import com.org.cafemgmt.exceptionhandlers.CafeEntityNotFoundException;
import com.org.cafemgmt.exceptionhandlers.CafeInvalidContentTypeException;
import com.org.cafemgmt.exceptionhandlers.CafeInvalidParameterException;
import com.org.cafemgmt.model.CafeMenuItems;
import com.org.cafemgmt.service.MenuItemsService;
import com.org.cafemgmt.service.MenuService;
import com.org.cafemgmt.views.CafeMenuItemView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MultipartFile;

import javax.xml.ws.Response;
import java.util.Optional;

@RestController
public class RestMenuItemsController {

    @Autowired
    MenuService menuService;

    @Autowired
    MenuItemsService menuItemsService;

    @RequestMapping(value = "/api/menus/items", method = RequestMethod.GET)
    @JsonView(CafeMenuItemView.ViewToReturnMenuItems.class)
    public ResponseEntity<Object> listAllMenuItems() {
        return ResponseEntity.status(200).body(menuItemsService.getAllMenuItems());
    }

    @RequestMapping(value = "/api/menus/items/{id}", method = RequestMethod.GET)
    @JsonView(CafeMenuItemView.ViewToReturnMenuItems.class)
    public ResponseEntity<Object> listOneItem(@PathVariable("id") long id) {
        CafeMenuItems cafeMenuItem = menuItemsService.getMenuItemById(id);
        if (cafeMenuItem == null) {
            throw new CafeEntityNotFoundException("No menu item found with id: " + id);
        }
        return ResponseEntity.status(200).body(cafeMenuItem);
    }

    @RequestMapping(value = "/api/menus/items/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Object> deleteMenuItem(@PathVariable long id) {
        menuItemsService.deleteMenuItem(id);
        return ResponseEntity.status(204).build();
    }

    @RequestMapping(value = "/api/menus/items", method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @JsonView(CafeMenuItemView.ViewToReturnMenuItems.class)
    public ResponseEntity<Object> createMenuItem(WebRequest request, @RequestPart("image") MultipartFile file,
                                                 @RequestPart("name") String menuItemName,
                                                 @RequestPart("description") String menuItemDescription,
                                                 @RequestPart("price") String price) {

        if (menuItemName == null || menuItemDescription == null || file == null) {
            throw new CafeInvalidParameterException("Name, Description or Image of Menu Item cannot be empty");
        }

        CafeMenuItems cafeMenuItem = new CafeMenuItems();
        cafeMenuItem.setName(menuItemName);
        cafeMenuItem.setDescription(menuItemDescription);
        cafeMenuItem.setPrice(Double.parseDouble(price));
        String imagePath = menuItemsService.saveMenuItemImage(file);
        if (imagePath != null) {
            cafeMenuItem.setImagePath(imagePath);
            return ResponseEntity.status(201).body(menuItemsService.saveMenuItem(cafeMenuItem));
        }
        else {
            throw new CafeInvalidParameterException("Image Path invalid. Allowed [ jpg, jpeg, png, gif, bmp ]");
        }

    }

}

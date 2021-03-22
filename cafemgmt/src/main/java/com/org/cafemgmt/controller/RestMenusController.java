package com.org.cafemgmt.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.org.cafemgmt.model.CafeMenus;
import com.org.cafemgmt.model.CafeUsers;
import com.org.cafemgmt.service.MenuService;
import com.org.cafemgmt.views.CafeMenuView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class RestMenusController {

    @Autowired
    MenuService menuService;

    @RequestMapping(value = "/api/menus", method = RequestMethod.GET)
    @JsonView(CafeMenuView.ViewToReturnMenus.class)
    public ResponseEntity<Object> listAllMenus() {
        return ResponseEntity.status(200).body(menuService.listAllMenus());
    }
}

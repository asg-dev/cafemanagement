package com.org.cafemgmt.rest;

import com.org.cafemgmt.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
public class RestReportsController {
    @Autowired
    UserService userService;

    @RequestMapping(value = "/search/users", method = RequestMethod.POST)
    public ResponseEntity<Object> searchCustomerName(@RequestParam("customer") String isCustomer, @RequestParam("q") String queryString) {
        if (Boolean.parseBoolean(isCustomer)) {
            Map<Long, String> userMap = userService.searchUser(queryString, false);
            return ResponseEntity.status(200).body(userMap);
        } else {
            Map<Long, String> userMap = userService.searchUser(queryString, true);
            return ResponseEntity.status(200).body(userMap);
        }
    }
}

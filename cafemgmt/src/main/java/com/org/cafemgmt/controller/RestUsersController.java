package com.org.cafemgmt.controller;


import com.fasterxml.jackson.annotation.JsonView;
import com.org.cafemgmt.model.CafeUsers;
import com.org.cafemgmt.service.UserService;
import com.org.cafemgmt.views.CafeUserView;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
public class RestUsersController {
    @Autowired
    UserService userService;


    @JsonView(CafeUserView.ViewToReturnUsers.class)
    @RequestMapping(value = "/api/users", method = RequestMethod.GET)
    public List<CafeUsers> listAllUsers() {
        List<CafeUsers> allUsers = userService.listAllUserDetails();
        return allUsers;
    }

    @JsonView(CafeUserView.ViewToReturnUsers.class)
    @RequestMapping(value = "/api/users", method = RequestMethod.POST)
    public CafeUsers addUser(@RequestBody CafeUsers user) {
        try {
            return userService.saveUser(user);
        }
        catch (InvalidParameterException | MessagingException e) {
            log.info("Exception: ", e);
        }
        return null;
    }

    @RequestMapping(value = "/api/users/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Object> deleteUser(@PathVariable long id) {
        Optional<CafeUsers> userToDelete = userService.findUserById(id);
        if (!userToDelete.isPresent()){
            return ResponseEntity.status(404).body("User Not Found");
        }
        userService.deleteUser(userToDelete.get());
        return ResponseEntity.status(204).body("");
    }

    @RequestMapping(value = "/api/users/{id}", method = RequestMethod.PUT)
    public ResponseEntity<Object> editUser(@Validated @RequestBody CafeUsers cafeUser, @PathVariable long id) {
        Optional<CafeUsers> myUser = userService.findUserById(id);
        List<String> acceptedAuthorities = new ArrayList<String>();
        acceptedAuthorities.add("ROLE_CLERK");
        acceptedAuthorities.add("ROLE_ADMIN");
        if (!myUser.isPresent()) {
            return ResponseEntity.status(404).body("User Not Found");
        }
//        if (!acceptedAuthorities.contains(cafeUser.getAuthority())) {
//            return ResponseEntity.status(400).body("Invalid Authority in Request. Authority can't be null or ROLE_CUSTOMER");
//        }
        cafeUser.setId(id);
        return ResponseEntity.status(200).body(userService.apiUpdateUser(cafeUser));
    }

    @JsonView(CafeUserView.ViewToReturnUsers.class)
    @RequestMapping(value = "/api/users/{id}", method = RequestMethod.GET)
    public ResponseEntity<Object> getUser(@PathVariable long id) {
        Optional<CafeUsers> user = userService.findUserById(id);
        if (!user.isPresent()) {
            return ResponseEntity.status(404).body("User Not Found");
        }
        return ResponseEntity.status(200).body(user.get());
    }

}

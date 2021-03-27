package com.org.cafemgmt.rest;


import com.fasterxml.jackson.annotation.JsonView;
import com.org.cafemgmt.exceptionhandlers.CafeEntityNotFoundException;
import com.org.cafemgmt.exceptionhandlers.CafeInvalidParameterException;
import com.org.cafemgmt.model.CafeUsers;
import com.org.cafemgmt.service.UserService;
import com.org.cafemgmt.views.CafeUserView;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
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
            if (user != null && user.getAuthority() != null) {
                if (!user.getAuthority().contains("ROLE_CLERK") && !user.getAuthority().contains("ROLE_ADMIN")) {
                    throw new CafeInvalidParameterException("Invalid Parameter in Request - User authority should be ROLE_CLERK or ROLE_ADMIN");
                }
                return userService.saveUser(user);
            }
            else {
                throw new CafeInvalidParameterException("Name, emailAddress and authority fields are mandatory");
            }
        }
        catch (MessagingException e) {
            log.info("MessagingException: ", e);
        }
        return null;
    }

    @RequestMapping(value = "/api/users/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Object> deleteUser(@PathVariable long id) {
        Optional<CafeUsers> userToDelete = userService.findUserById(id);
        if (!userToDelete.isPresent()) {
            log.error("User not found, throwing Exception");
            throw new CafeEntityNotFoundException("No User Found with id "+id);
        }
        userService.deleteUser(userToDelete.get());
        return ResponseEntity.status(204).body("");
    }

    @RequestMapping(value = "/api/users/{id}", method = RequestMethod.PUT)
    @JsonView(CafeUserView.ViewToReturnUsers.class)
    public ResponseEntity<Object> editUser(@RequestBody CafeUsers cafeUser, @PathVariable long id) {
        Optional<CafeUsers> myUser = userService.findUserById(id);
        List<String> acceptedAuthorities = new ArrayList<String>();
        acceptedAuthorities.add("ROLE_CLERK");
        acceptedAuthorities.add("ROLE_ADMIN");
        if (!myUser.isPresent()) {
            throw new CafeEntityNotFoundException("No User Found with id "+id);
        }
        if (cafeUser == null || (cafeUser.getAuthority() != null &&
                ! acceptedAuthorities.contains(cafeUser.getAuthority())) ) {
            throw new CafeInvalidParameterException("Invalid Parameters in Request. Authority should be ROLE_ADMIN or ROLE_CLERK");
        }
        cafeUser.setId(id);
        return ResponseEntity.status(200).body(userService.apiUpdateUser(cafeUser));
    }

    @JsonView(CafeUserView.ViewToReturnUsers.class)
    @RequestMapping(value = "/api/users/{id}", method = RequestMethod.GET)
    public ResponseEntity<Object> getUser(@PathVariable long id) {
        Optional<CafeUsers> user = userService.findUserById(id);
        if (!user.isPresent()) {
            throw new CafeEntityNotFoundException("No User Found with id "+id);
        }
        return ResponseEntity.status(200).body(user.get());
    }

}

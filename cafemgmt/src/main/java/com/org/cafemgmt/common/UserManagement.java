package com.org.cafemgmt.common;

import com.org.cafemgmt.model.CafeUsers;
import com.org.cafemgmt.model.PrincipalUserDetails;
import com.org.cafemgmt.service.UserService;
import org.springframework.security.core.Authentication;

public class UserManagement {

    public static String getUserName(Authentication authentication, UserService userService) {
        PrincipalUserDetails currentUser = (PrincipalUserDetails) authentication.getPrincipal();
        String userEmail = currentUser.getUsername();
        CafeUsers cafeUser = userService.findUserByEmail(userEmail);
        if (cafeUser != null) {
            return cafeUser.getName();
        }
        return null;
    }

    public static String getAuthority(Authentication authentication, UserService userService) {
        return (userService.findUserByEmail(((PrincipalUserDetails) authentication.getPrincipal()).getUsername())).getAuthority();
    }

}

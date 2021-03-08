package com.org.cafemgmt.service;

import com.org.cafemgmt.model.CafeUsers;

import java.util.List;

public interface UserService {
    public CafeUsers findUserByEmail(String emailaddress);
    public List<CafeUsers> listAllUserDetails();
    public void insertUser(CafeUsers cafeUsers);
    public boolean authenticateUser(String rawPassword, String hashedPassword);
}

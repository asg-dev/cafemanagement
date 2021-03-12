package com.org.cafemgmt.service;

import com.org.cafemgmt.model.CafeUsers;
import com.org.cafemgmt.repository.UserRepository;

import java.util.List;
import java.util.Optional;

public interface UserService {
    public CafeUsers findUserByEmail(String emailaddress);
    public CafeUsers findUserById(long id);
    public List<CafeUsers> listAllUserDetails();
    public void insertUser(CafeUsers cafeUsers);
    public void insertInternalUser(CafeUsers cafeUsers);
    public void updateInternalUser(CafeUsers cafeUsers);
    public boolean authenticateUser(String rawPassword, String hashedPassword);
}

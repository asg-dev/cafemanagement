package com.org.cafemgmt.service;

import com.org.cafemgmt.model.CafeUsers;
import com.org.cafemgmt.repository.UserRepository;

import javax.mail.MessagingException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public interface UserService {
    public CafeUsers findUserByEmail(String emailaddress);

    public Optional<CafeUsers> findUserById(long id);

    public List<CafeUsers> listAllUserDetails();

    public void signupUser(CafeUsers cafeUsers);

    public CafeUsers saveCafeUser(CafeUsers cafeUsers, String method);

    public void deleteUser(CafeUsers cafeUser);

    public CafeUsers findUserByValidityToken(UUID token);

    public CafeUsers registerUser(CafeUsers cafeUsers); // perform password save.

    public boolean authenticateUser(String rawPassword, String hashedPassword);

    public CafeUsers apiUpdateUser(CafeUsers cafeUser);

    public long getWalkinCustomerId();

    public void triggerPasswordReset(CafeUsers cafeUser) throws MessagingException;

    public Map<Long, String> searchUser(String queryString, boolean b);

    public long getUserCount(String authority);

    public String getHashedPassword(String password);
}

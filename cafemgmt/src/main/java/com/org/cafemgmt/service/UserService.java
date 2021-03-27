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

    public void insertUser(CafeUsers cafeUsers);

    public void insertInternalUser(CafeUsers cafeUsers);

    public void updateInternalUser(CafeUsers cafeUsers);

    public CafeUsers saveUser(CafeUsers user) throws MessagingException;

    public void deleteUser(CafeUsers cafeUser);

    public void insertCustomUser(CafeUsers cafeUsers) throws MessagingException;

    public CafeUsers findUserByValidityToken(UUID token);

    public void registerUser(CafeUsers cafeUsers); // does password save alone.

    public boolean authenticateUser(String rawPassword, String hashedPassword);

    public CafeUsers apiUpdateUser(CafeUsers cafeUser);

    public long getWalkinCustomerId();

    public void triggerPasswordReset(CafeUsers cafeUser) throws MessagingException;

    public Map<Long, String> searchUser(String queryString, boolean b);
}

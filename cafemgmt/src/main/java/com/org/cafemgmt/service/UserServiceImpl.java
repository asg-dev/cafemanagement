package com.org.cafemgmt.service;

import com.org.cafemgmt.model.CafeUsers;
import com.org.cafemgmt.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;
    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public CafeUsers findUserByEmail(String emailaddress) {
        return userRepository.findByEmailAddress(emailaddress);
    }

    @Override
    public CafeUsers findUserById(long id) {
        return userRepository.findUserById(id);
    }

    @Override
    public List<CafeUsers> listAllUserDetails() {
        return userRepository.findAll();
    }

    @Override
    public void insertUser(CafeUsers cafeUsers) {
        cafeUsers.setCreated_at(new Date());
        cafeUsers.setUpdated_at(new Date());
        cafeUsers.setAuthority("ROLE_CUSTOMER");
        cafeUsers.setInternalUser(false);
        cafeUsers.setWalkinCustomer(false);
        cafeUsers.setPassword(getHashedPassword(cafeUsers.getPassword()));
        userRepository.save(cafeUsers);
    }

    @Override
    public void updateInternalUser(CafeUsers cafeUsers) {
        CafeUsers existingUser = userRepository.findUserById(cafeUsers.getId());
        if (existingUser != null) {
            Date createdTime = existingUser.getCreated_at();
            cafeUsers.setCreated_at(createdTime);
            String password = existingUser.getPassword();
            cafeUsers.setPassword(password);
        }
        cafeUsers.setUpdated_at(new Date());
        userRepository.save(cafeUsers);
    }

    @Override
    public void insertInternalUser(CafeUsers cafeUsers) {
        cafeUsers.setCreated_at(new Date());
        cafeUsers.setUpdated_at(new Date());
        cafeUsers.setInternalUser(true);
        cafeUsers.setWalkinCustomer(false);
        // user will be saved without a password. We have to send an email and ask them to set the password - javamailer
        userRepository.save(cafeUsers);
    }

    @Override
    public boolean authenticateUser(String rawPassword, String hashedPassword) {
        boolean authenticated = false;
        if (bCryptPasswordEncoder.matches(rawPassword, hashedPassword)) {
            authenticated = true;
        }
        return authenticated;
    }

    private String getHashedPassword(String password) {
        return bCryptPasswordEncoder.encode(password);
    }
}

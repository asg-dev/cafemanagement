package com.org.cafemgmt.service;

import com.org.cafemgmt.model.CafeUsers;
import com.org.cafemgmt.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

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
    public List<CafeUsers> listAllUserDetails() {
        // log.info(String.valueOf(userRepository.findAll().get(0).getId()));
        return null;
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

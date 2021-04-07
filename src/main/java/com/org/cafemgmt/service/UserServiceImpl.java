package com.org.cafemgmt.service;

import com.org.cafemgmt.model.CafeUsers;
import com.org.cafemgmt.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.util.*;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    EmailService emi;

    @Autowired
    UserRegistration userRegistration;

    @Override
    public CafeUsers findUserByEmail(String emailaddress) {
        return userRepository.findByEmailAddress(emailaddress);
    }

    @Override
    public Optional<CafeUsers> findUserById(long id) {
        return userRepository.findById(id);
    }

    @Override
    public List<CafeUsers> listAllUserDetails() {
        return userRepository.findAll();
    }

    @Override
    public void deleteUser(CafeUsers cafeUser) {
        userRepository.deleteById(cafeUser.getId());
        userRepository.flush();
    }

    @Override
    public void signupUser(CafeUsers cafeUsers) { // used in Signup flow
        // After completion, users should always follow two-step verification - where active will be set to true.
        cafeUsers.setUpdated_at(new Date());
        cafeUsers.setActive(true);
        cafeUsers.setAuthority("ROLE_CUSTOMER");
        cafeUsers.setInternalUser(false);
        cafeUsers.setPassword(getHashedPassword(cafeUsers.getPassword()));
        userRepository.save(cafeUsers);
    }

    @Override
    public CafeUsers findUserByValidityToken(UUID token) {
        return userRepository.findUserByValidityToken(token);
    }

    @Override
    public CafeUsers registerUser(CafeUsers cafeUsers) {
        CafeUsers existingUser = userRepository.findByEmailAddress(cafeUsers.getEmailAddress());
        if (existingUser != null) {
            log.info("Registering new user with (name): " + cafeUsers.getName());
            log.info("Registering new user with (email): " + cafeUsers.getEmailAddress());
            existingUser.setPassword(getHashedPassword(cafeUsers.getPassword()));
            existingUser.setName(cafeUsers.getName());
            existingUser.setValidityToken(generateUUID()); // setting a new validity token to expire the old link.
            existingUser.setActive(true);
            return userRepository.save(existingUser);
        } else {
            throw new IllegalStateException();
        }
    }

    @Override
    public CafeUsers saveCafeUser(CafeUsers cafeUsers, String method) {
        if (method.equals("UPDATE")) {
            CafeUsers existingUser = userRepository.findUserById(cafeUsers.getId());
            if (existingUser != null) {
                Date createdTime = existingUser.getCreated_at();
                cafeUsers.setCreated_at(createdTime);
                String password = existingUser.getPassword();
                cafeUsers.setPassword(password);
                cafeUsers.setActive(existingUser.isActive());
                cafeUsers.setValidityToken(existingUser.getValidityToken());
                cafeUsers.setUpdated_at(new Date());
                if (!existingUser.getEmailAddress().equals(cafeUsers.getEmailAddress())) {
                    triggerRegistrationEmail(cafeUsers);
                }
                userRepository.save(cafeUsers);
            }
        }
        else if (method.equals("CREATE")) {
            cafeUsers.setCreated_at(new Date());
            cafeUsers.setUpdated_at(new Date());
            if (cafeUsers.getAuthority().contains("ROLE_CUSTOMER")) {
                cafeUsers.setInternalUser(false);
            }
            else {
                cafeUsers.setInternalUser(true);
            }

            cafeUsers.setActive(false);
            cafeUsers.setValidityToken(generateUUID());
            triggerRegistrationEmail(cafeUsers);
            userRepository.save(cafeUsers);
        }
        return cafeUsers;
    }

    @Override
    public boolean authenticateUser(String rawPassword, String hashedPassword) {
        boolean authenticated = false;
        if (bCryptPasswordEncoder.matches(rawPassword, hashedPassword)) {
            authenticated = true;
        }
        return authenticated;
    }

    @Override
    public CafeUsers apiUpdateUser(CafeUsers cafeUser) {
        Optional<CafeUsers> existingUser = userRepository.findById(cafeUser.getId());
        if (existingUser.isPresent()) {
            CafeUsers existingUserObj = existingUser.get();
            if (cafeUser.getName() != null) {
                existingUserObj.setName(cafeUser.getName());
            }
            if (cafeUser.getEmailAddress() != null) {
                if (! cafeUser.getEmailAddress().equals(existingUser.get().getEmailAddress())) {
                    existingUserObj.setEmailAddress(cafeUser.getEmailAddress());
                    existingUserObj.setValidityToken(generateUUID());
                    triggerRegistrationEmail(existingUserObj);
                } // if the email does not match, don't perform necessary updates
            }
            if (cafeUser.getAuthority() != null) {
                existingUserObj.setAuthority(cafeUser.getAuthority());
            }
            return userRepository.save(existingUserObj);
        }
        return null;
    }

    @Override
    public long getWalkinCustomerId() {
        CafeUsers walkinUser = userRepository.findByEmailAddress("walkincustomerdefault@freshbrew.com");
        if (walkinUser != null) {
            return walkinUser.getId();
        } else {
            return -1;
        }
    }

    @Override
    public void triggerPasswordReset(CafeUsers cafeUser) throws MessagingException {
        CafeUsers resetUser = userRepository.findByEmailAddress(cafeUser.getEmailAddress());
        if (resetUser != null) {
            resetUser.setValidityToken(generateUUID());
            userRepository.save(resetUser);
            emi.sendMessage(resetUser.getEmailAddress(), "Reset Password for Freshbrew Cafe Management", userRegistration.generatePasswordResetMessage(
                    resetUser.getValidityToken(), resetUser.getName()
            ));
        }
    }

    @Override
    public Map<Long, String> searchUser(String queryString, boolean b) {
        List<CafeUsers> cafeUsers = userRepository.searchUsers(queryString.toUpperCase(), b);
        Map<Long, String> userMap = new HashMap<Long, String>();
        for (CafeUsers user : cafeUsers) {
            userMap.put(user.getId(), user.getName());
        }
        return userMap;
    }

    @Override
    public long getUserCount(String authority) {
        return userRepository.findAdminCount(authority);
    }

    private void triggerRegistrationEmail(CafeUsers cafeUsers) {
        try {
            emi.sendMessage(cafeUsers.getEmailAddress(), "Your Freshbrew Cafe Management Registration :)", userRegistration.generateRegistrationMessage(
                    cafeUsers.getValidityToken(), cafeUsers.getName()
            ));
        } catch (MessagingException messagingException) {
            log.info("Couldn't send email. Saving user without registration. Exception thrown: " + messagingException.getCause());
        }
    }

    @Override
    public String getHashedPassword(String password) {
        return bCryptPasswordEncoder.encode(password);
    }

    private UUID generateUUID() {
        return new UUID(1, 1).randomUUID();
    }

}

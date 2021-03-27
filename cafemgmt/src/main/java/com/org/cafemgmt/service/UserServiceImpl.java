package com.org.cafemgmt.service;

import com.org.cafemgmt.model.CafeUsers;
import com.org.cafemgmt.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
    public CafeUsers saveUser(CafeUsers user) throws MessagingException { // RestUsersController to save API POST /api/users - POST
        user.setCreated_at(new Date());
        user.setUpdated_at(new Date());
        if (user.getAuthority() == null) {
            user.setAuthority("ROLE_CLERK");
        }

        user.setWalkinCustomer(false);
        user.setInternalUser(true); // all API user creation applies only for internal users.
        user.setValidityToken(generateUUID());
        emi.sendMessage(user.getEmailAddress(), "Your Freshbrew Cafe Management Registration :)", userRegistration.generateRegistrationMessage(
                user.getValidityToken(), user.getName()
        ));
        return userRepository.save(user);
    }

    @Override
    public void insertUser(CafeUsers cafeUsers) { // used in Signup flow - not ideal but useful for testing
        // After completion, users should always follow two-step verification - where active will be set to true.
        cafeUsers.setCreated_at(new Date());
        cafeUsers.setUpdated_at(new Date());
        cafeUsers.setActive(true);
        cafeUsers.setAuthority("ROLE_CUSTOMER");
        cafeUsers.setInternalUser(false);
        cafeUsers.setWalkinCustomer(false);
        cafeUsers.setPassword(getHashedPassword(cafeUsers.getPassword()));
        userRepository.save(cafeUsers);
    }

    @Override
    public void insertCustomUser(CafeUsers cafeUsers) throws MessagingException {
            cafeUsers.setCreated_at(new Date());
            cafeUsers.setUpdated_at(new Date());
            cafeUsers.setInternalUser(false);
            cafeUsers.setWalkinCustomer(false);
            cafeUsers.setActive(false);
            cafeUsers.setValidityToken(generateUUID());
        try {
            emi.sendMessage(cafeUsers.getEmailAddress(), "Your Freshbrew Cafe Management Registration :)", userRegistration.generateRegistrationMessage(
                    cafeUsers.getValidityToken(), cafeUsers.getName()
            ));
        }
        catch (MessagingException messagingException) {
            log.info("Couldn't send email. Saving user without registration. Exception thrown: " + messagingException.getCause());
        }

        userRepository.save(cafeUsers);
    }

    @Override
    public CafeUsers findUserByValidityToken(UUID token) {
        return userRepository.findUserByValidityToken(token);
    }

    @Override
    public void registerUser(CafeUsers cafeUsers) {
        CafeUsers existingUser = userRepository.findByEmailAddress(cafeUsers.getEmailAddress());
        if (existingUser != null) {
            log.info("cafeUsers (name): " + cafeUsers.getName());
            log.info("cafeUsers (email): " + cafeUsers.getEmailAddress());
            log.info("cafeUsers (rawPassword): " + cafeUsers.getPassword());

            existingUser.setPassword(getHashedPassword(cafeUsers.getPassword()));

            log.info("ID: " + cafeUsers.getId() + " ::: ID EXISTING: " + existingUser.getId());
            existingUser.setValidityToken(generateUUID()); // setting a new validity token to expire the old link.
            existingUser.setActive(true);
            userRepository.save(existingUser);
        }
        else {
            throw new IllegalStateException();
        }
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
        cafeUsers.setValidityToken(generateUUID());
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

    @Override
    public CafeUsers apiUpdateUser(CafeUsers cafeUser) {
        Optional<CafeUsers> existingUser = userRepository.findById(cafeUser.getId());
        if (existingUser.isPresent()) {
            CafeUsers existingUserObj = existingUser.get();
            if (cafeUser.getName() != null) { existingUserObj.setName(cafeUser.getName()); }
            if (cafeUser.getEmailAddress() != null) { existingUserObj.setEmailAddress(cafeUser.getEmailAddress()); }
            if (cafeUser.getAuthority() != null) { existingUserObj.setAuthority(cafeUser.getAuthority()); }
            return userRepository.save(existingUserObj);
        }
        return null;
    }

    @Override
    public long getWalkinCustomerId() {
        CafeUsers walkinUser = userRepository.findByEmailAddress("walkincustomerdefault@freshbrew.com");
        if (walkinUser != null) {
            return walkinUser.getId();
        }
        else {
            return -1;
        }

    }

    @Override
    public void triggerPasswordReset(CafeUsers cafeUser) throws MessagingException {
        CafeUsers resetUser = userRepository.findByEmailAddress(cafeUser.getEmailAddress());
        if (resetUser != null) {
            resetUser.setValidityToken(generateUUID());
            userRepository.save(resetUser);
            emi.sendMessage(resetUser.getEmailAddress(), "Reset Password for Freshbrew Cafe Management", userRegistration.generatePasswordResetMessage (
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


    private String getHashedPassword(String password) {
        return bCryptPasswordEncoder.encode(password);
    }

    private UUID generateUUID() { return new UUID(1,1).randomUUID(); }

}

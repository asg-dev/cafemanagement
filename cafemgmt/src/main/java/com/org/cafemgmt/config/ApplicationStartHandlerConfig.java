package com.org.cafemgmt.config;

import com.org.cafemgmt.model.CafeUsers;
import com.org.cafemgmt.repository.UserRepository;
import com.org.cafemgmt.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.UUID;

@Component
@Slf4j
public class ApplicationStartHandlerConfig implements CommandLineRunner {

    @Autowired
    UserRepository userRepository;
    @Autowired
    UserService userService;

    @Override
    public void run(String... args) throws Exception {

        CafeUsers existingUser = userRepository.findByEmailAddress("walkincustomerdefault@freshbrew.com");
        if (existingUser == null) {
            CafeUsers cafeUsers = new CafeUsers();
            cafeUsers.setEmailAddress("walkincustomerdefault@freshbrew.com");
            cafeUsers.setAuthority("ROLE_CUSTOMER");
            cafeUsers.setName("Walk-in Customer");
            cafeUsers.setValidityToken(new UUID(1, 0));
            cafeUsers.setActive(true);
            cafeUsers.setCreated_at(new Date());
            cafeUsers.setUpdated_at(new Date());
            userRepository.save(cafeUsers);
            log.info("User for Walk-in Customer successfully added");
        } else {
            log.info("Walk-in Customer exists with id " + existingUser.getId());
        }

        long adminCount = userRepository.findAdminCount("ROLE_ADMIN");
        if (adminCount == 0) {
            CafeUsers cafeUsers = new CafeUsers();
            cafeUsers.setEmailAddress("admin@freshbrew.com");
            cafeUsers.setAuthority("ROLE_ADMIN");
            cafeUsers.setName("Freshbrew Admin");
            cafeUsers.setValidityToken(new UUID(1, 0));
            cafeUsers.setActive(true);
            cafeUsers.setInternalUser(true);
            cafeUsers.setCreated_at(new Date());
            cafeUsers.setUpdated_at(new Date());
            cafeUsers.setPassword(userService.getHashedPassword("admin@123"));
            userRepository.save(cafeUsers);
            log.info("Default admin added");
        }
        else {
            log.info("Admin exists. Not creating default admin");
        }
    }
}

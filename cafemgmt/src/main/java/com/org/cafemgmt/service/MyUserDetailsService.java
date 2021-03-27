package com.org.cafemgmt.service;

import com.org.cafemgmt.model.PrincipalUserDetails;
import com.org.cafemgmt.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import com.org.cafemgmt.model.CafeUsers;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class MyUserDetailsService implements UserDetailsService {
    @Autowired
    UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        log.info("Loading user: ", s);
        CafeUsers cafeUser = userRepository.findByEmailAddress(s);
        if (cafeUser == null) {
            throw new UsernameNotFoundException("User Not Found!");
        }
        UserDetails user = new PrincipalUserDetails(cafeUser);
        log.info("Authority: ", user.getAuthorities());
        return user;
    }
}

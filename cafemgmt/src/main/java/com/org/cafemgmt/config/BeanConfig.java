package com.org.cafemgmt.config;

import com.org.cafemgmt.service.MyUserDetailsService;
import com.org.cafemgmt.service.UserService;
import com.org.cafemgmt.service.UserServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
public class BeanConfig {

    @Bean
    UserService userService() {
        return new UserServiceImpl();
    }

}

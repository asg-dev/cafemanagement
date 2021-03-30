package com.org.cafemgmt.config;

import com.org.cafemgmt.service.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Properties;

@Configuration
public class BeanConfig {

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    UserService userService() {
        return new UserServiceImpl();
    }

    @Bean
    public JavaMailSender getJavaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost("smtp-mail.outlook.com");
        mailSender.setPort(587);

        mailSender.setUsername("assist.kingsguard@outlook.com");
        mailSender.setPassword("Fduser@123");

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.debug", "true");

        return mailSender;
    }

    @Bean
    EmailService emailService() {
        return new EmailServiceImpl();
    }

    @Bean
    UserRegistration userRegistration() {
        return new UserRegistrationImpl();
    }

    @Bean
    MenuService menuService() {
        return new MenuServiceImpl();
    }

    @Bean
    MenuItemsService menuItemsService() {
        return new MenuItemsServiceImpl();
    }

    @Bean
    CartsService cartsService() {
        return new CartsServiceImpl();
    }

    @Bean
    CafeOrderService cafeOrderService() {
        return new CafeOrderServiceImpl();
    }

    @Bean
    CafeRatingsService cafeRatingsService() {
        return new CafeRatingsServiceImpl();
    }


}

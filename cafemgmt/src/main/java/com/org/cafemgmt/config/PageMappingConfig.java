package com.org.cafemgmt.config;

import org.hibernate.criterion.Order;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

public class PageMappingConfig implements WebMvcConfigurer {

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/users/{id}/edit").setViewName("edit.html");
        registry.setOrder(Ordered.HIGHEST_PRECEDENCE);
    }
}

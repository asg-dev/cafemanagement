package com.org.cafemgmt.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;
import java.util.List;

@Slf4j
@Component
public class CafeAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
        Collection<? extends GrantedAuthority> grantedAuthorityList = authentication.getAuthorities();
        grantedAuthorityList.forEach(authority -> {
            if(authority.getAuthority().equals("ROLE_CUSTOMER")) {
                try {
                    redirectStrategy.sendRedirect(httpServletRequest, httpServletResponse, "/menus");
                }
                catch (Exception e) {
                    log.error("Exception Occurred", e);
                }
            }
            else if (authority.getAuthority().equals("ROLE_ADMIN")) {
                try {
                    redirectStrategy.sendRedirect(httpServletRequest, httpServletResponse, "/admin");
                }
                catch (Exception e) {
                    log.error("Exception Occurred", e);
                }
            }
            else if (authority.getAuthority().equals("ROLE_CLERK")) {
                try {
                    redirectStrategy.sendRedirect(httpServletRequest, httpServletResponse, "/orders");
                }
                catch (Exception e) {
                    log.error("Exception Occurred", e);
                }
            }
            else {
                throw new IllegalStateException();
            }
        });
    }
}

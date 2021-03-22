package com.org.cafemgmt.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;

@Slf4j
@Component
public class CafeAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
        Collection<? extends GrantedAuthority> grantedAuthorityList = authentication.getAuthorities();
        grantedAuthorityList.forEach(authority -> {
            try {
                if (authority.getAuthority().equals("ROLE_CUSTOMER")) {
                    redirectStrategy.sendRedirect(httpServletRequest, httpServletResponse, "/site/menus");
                } else if (authority.getAuthority().equals("ROLE_ADMIN")) {
                    redirectStrategy.sendRedirect(httpServletRequest, httpServletResponse, "/admin");
                } else if (authority.getAuthority().equals("ROLE_CLERK")) {
                    redirectStrategy.sendRedirect(httpServletRequest, httpServletResponse, "/pending_orders");
                } else {
                    throw new IllegalStateException();
                }
            }
            catch (IOException ioException) {
                log.info("IOException occurred", ioException.getMessage());
            }
            catch (IllegalStateException illegalStateException) {
                log.info("IllegalStateException occurred: ", illegalStateException.getMessage());
            }
        });
    }
}

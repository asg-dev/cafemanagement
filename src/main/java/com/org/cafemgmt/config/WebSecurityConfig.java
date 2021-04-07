package com.org.cafemgmt.config;

import com.org.cafemgmt.service.MyUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private MyUserDetailsService userDetailsService;

    @Autowired
    private CafeAuthenticationSuccessHandler successHandler;

    @Autowired
    private CafeAuthenticationFailureHandler failureHandler;

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/admin", "/reports/**", "/generate_report", "/menus/**").hasAuthority("ROLE_ADMIN")
                .antMatchers("/pending_orders").hasAnyAuthority("ROLE_ADMIN", "ROLE_CLERK")
                .antMatchers("/site/my_orders").hasAuthority("ROLE_CUSTOMER")
                .antMatchers("/api/users/**", "/api/menus/**", "/api/menus/items/**").hasAuthority("ROLE_ADMIN")
                .antMatchers("/api/orders/**").hasAnyAuthority("ROLE_ADMIN", "ROLE_CLERK")
                .antMatchers("/menus", "/users", "/users/**", "/site/**").authenticated()
                .antMatchers("/api/users/**", "/api/menus/**", "/api/carts/**", "/api/menus/items/**").authenticated()
                .antMatchers("/", "/login").permitAll()
                .and()
                .formLogin()
                .loginPage("/login")
                .permitAll()
                .successHandler(successHandler)
                .and()
                .httpBasic()
                .and()
                .cors()
                .and()
                .csrf().disable();
    }

}

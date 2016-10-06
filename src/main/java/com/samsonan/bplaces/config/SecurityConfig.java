package com.samsonan.bplaces.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import org.springframework.security.core.userdetails.UserDetailsService;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private UserDetailsService userService;
    
    @Autowired
    public SecurityConfig(UserDetailsService userService){
        this.userService = userService;
    }
    
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService);       
    }
    
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .csrf().disable()   //TODO: enable later
            .authorizeRequests()
                .antMatchers("/", "/map").permitAll()
                .antMatchers("/resources/**", "/webjars/**").permitAll()
                .antMatchers("/places/add").access("hasRole('ADMIN')")
                .antMatchers("/places/list").access("hasRole('ADMIN')")
                .antMatchers("/places/**/edit").access("hasRole('ADMIN')")
                .antMatchers("/places/**/delete").access("hasRole('ADMIN')")
                .antMatchers("/places/**").permitAll()
                .antMatchers("/images/**").permitAll()// TODO:test only
                .antMatchers("/api/**").permitAll()
                .anyRequest().authenticated()
                .and()
            .formLogin()
                .loginPage("/login")
                .permitAll()
                .and()
            .rememberMe()
                .tokenValiditySeconds(1209600)
                .and()
            .logout()
                .permitAll();
    }
    
}

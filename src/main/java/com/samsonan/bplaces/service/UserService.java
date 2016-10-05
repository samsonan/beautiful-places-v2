package com.samsonan.bplaces.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.samsonan.bplaces.dao.UserDao;
import com.samsonan.bplaces.model.User;

@Service
public class UserService implements UserDetailsService {

    private UserDao userDao;

    @Autowired
    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        User domainUser = userDao.findByName(login);
        
        if (domainUser == null) 
            throw new UsernameNotFoundException("User with name "+login+" not found");
        
        boolean enabled = true;
        boolean accountNonExpired = true;
        boolean credentialsNonExpired = true;
        boolean accountNonLocked = true;

        return new org.springframework.security.core.userdetails.User(
                domainUser.getName(), 
                domainUser.getPassword(), 
                enabled, 
                accountNonExpired, 
                credentialsNonExpired, 
                accountNonLocked,
                getAuthorities(domainUser.getRole())
        );
    }
    
    public Collection<SimpleGrantedAuthority> getAuthorities(String role) {
        List<SimpleGrantedAuthority> authorities = new ArrayList<SimpleGrantedAuthority>();
        authorities.add(new SimpleGrantedAuthority(role));
        return authorities;
    }
    

}

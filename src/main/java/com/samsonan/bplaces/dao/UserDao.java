package com.samsonan.bplaces.dao;

import java.util.List;

import com.samsonan.bplaces.model.User;

public interface UserDao {

    List<User> findAll();

    User findById(int id);

    User findByName(String login);    
    
}

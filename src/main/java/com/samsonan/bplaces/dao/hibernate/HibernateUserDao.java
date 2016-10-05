package com.samsonan.bplaces.dao.hibernate;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.samsonan.bplaces.dao.UserDao;
import com.samsonan.bplaces.model.User;

@Repository
@Transactional
public class HibernateUserDao implements UserDao {

    private SessionFactory sessionFactory;
    
    @Autowired  
    public HibernateUserDao(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<User> findAll() {
        Session currentSession = sessionFactory.getCurrentSession();  
        
        Query query = currentSession.createQuery("from User");
        
        return query.list();
    }

    @Override
    public User findById(int id) {
        Session currentSession = sessionFactory.getCurrentSession();  
        
        return currentSession.get(User.class, id);
    }

    @Override
    public User findByName(String login) {
        Session currentSession = sessionFactory.getCurrentSession();  
        
        Query query = currentSession.createQuery("from User where name = :login")
                .setString("login", login);
        
        return (User) query.uniqueResult();
    }    
    
}

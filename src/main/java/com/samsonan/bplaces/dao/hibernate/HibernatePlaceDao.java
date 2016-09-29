package com.samsonan.bplaces.dao.hibernate;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.samsonan.bplaces.dao.PlaceDao;
import com.samsonan.bplaces.model.Place;

@Repository
@Transactional
public class HibernatePlaceDao implements PlaceDao {

    private SessionFactory sessionFactory;
    
    @Autowired  
    public HibernatePlaceDao(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Place> findAll() {
        Session currentSession = sessionFactory.getCurrentSession();  
        
        Query query = currentSession.createQuery("from Place");
        
        return query.list();
    }

}

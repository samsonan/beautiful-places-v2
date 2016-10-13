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
        Query query = currentSession.createQuery("from Place p left join p.images img with img.ordenal=0");
        return query.list();
    }

    @Override
    public Place findById(int id) {
        Session currentSession = sessionFactory.getCurrentSession();  
        return currentSession.get(Place.class, id);
    }

    @Override
    public void save(Place place) {
        Session currentSession = sessionFactory.getCurrentSession();
        currentSession.persist(place);
    }

    @Override
    public void update(Place place) {
        Session currentSession = sessionFactory.getCurrentSession();
        currentSession.merge(place);
    }
    
    
    @Override
    public void deleteById(int id) {

        Session currentSession = sessionFactory.getCurrentSession();

        Place place = currentSession.get(Place.class, id);
        
        currentSession.delete(place);
    }

    @Override
    public void delete(Place place) {

        Session currentSession = sessionFactory.getCurrentSession();
        
        currentSession.delete(place);
    }    
    
}

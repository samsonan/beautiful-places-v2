package com.samsonan.bplaces.dao.hibernate;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.samsonan.bplaces.dao.ImageDao;
import com.samsonan.bplaces.model.Image;

@Repository
@Transactional
public class HibernateImageDao implements ImageDao {

    private SessionFactory sessionFactory;
    
    @Autowired  
    public HibernateImageDao(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public List<Image> findAll() {
        
        Session currentSession = sessionFactory.getCurrentSession();  
        
        Query query = currentSession.createQuery("from Image");
        
        return query.list();
    }

    @Override
    public Image findById(int id) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void deleteById(int id) {
        // TODO Auto-generated method stub

    }

    @Override
    public void delete(Image image) {
        // TODO Auto-generated method stub

    }

    @Override
    public void save(Image image) {
        Session currentSession = sessionFactory.getCurrentSession();
        currentSession.persist(image);    
    }
    
    @Override
    public void update(Image image) {
        Session currentSession = sessionFactory.getCurrentSession();
        currentSession.merge(image);    
    }

}

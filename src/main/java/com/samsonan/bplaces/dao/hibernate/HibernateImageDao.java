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
    public Image findById(int placeId, int imageId) {
        Session currentSession = sessionFactory.getCurrentSession();  
        Query query = currentSession.createQuery("from Image where id= :imageId and place_id= :placeId")
                .setInteger("imageId", imageId)
                .setInteger("placeId", placeId);
        return (Image) query.uniqueResult();
    }

    @Override
    public void deleteById(int placeId, int imageId) {
        Session currentSession = sessionFactory.getCurrentSession();
        Image image = (Image) currentSession.createQuery("from Image where id= :imageId and place_id= :placeId")
                .setInteger("imageId", imageId)
                .setInteger("placeId", placeId).uniqueResult();
        currentSession.delete(image);
    }

    @Override
    public void delete(Image image) {

        Session currentSession = sessionFactory.getCurrentSession();
        currentSession.delete(image);
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

    @SuppressWarnings("unchecked")
    @Override
    public List<Image> findAllByPlaceId(int placeId) {
        Session currentSession = sessionFactory.getCurrentSession();  
        Query query = currentSession.createQuery("from Image where place_id= :placeId").
                setInteger("placeId", placeId);
        return query.list();
    }

}

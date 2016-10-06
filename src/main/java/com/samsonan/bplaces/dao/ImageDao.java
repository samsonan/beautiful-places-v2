package com.samsonan.bplaces.dao;

import java.util.List;

import com.samsonan.bplaces.model.Image;

public interface ImageDao {

    List<Image> findAll();

    Image findById(int id);

    void deleteById(int id);

    void delete(Image image);

    void save(Image image);

    void update(Image image);
    
}

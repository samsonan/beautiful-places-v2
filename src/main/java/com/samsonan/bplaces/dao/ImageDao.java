package com.samsonan.bplaces.dao;

import java.util.List;

import com.samsonan.bplaces.model.Image;

public interface ImageDao {

    List<Image> findAll();

    Image findById(int placeId, int imageId);

    void deleteById(int placeId, int imageId);

    void delete(Image image);

    void save(Image image);

    void update(Image image);

    List<Image> findAllByPlaceId(int placeId);
    
}

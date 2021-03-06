package com.samsonan.bplaces.dao;

import java.util.List;

import com.samsonan.bplaces.model.Place;

public interface PlaceDao {

    List<Place> findAll();

    Place findById(int id);

    void deleteById(int id);

    void delete(Place place);

    void save(Place place);

    void update(Place place);
    
}

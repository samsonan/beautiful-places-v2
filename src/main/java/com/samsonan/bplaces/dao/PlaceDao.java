package com.samsonan.bplaces.dao;

import java.util.List;

import com.samsonan.bplaces.model.Place;

public interface PlaceDao {

    List<Place> findAll();
    
}

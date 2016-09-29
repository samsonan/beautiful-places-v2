package com.samsonan.bplaces.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.samsonan.bplaces.dao.PlaceDao;
import com.samsonan.bplaces.model.Place;

@Service
public class PlaceService {

    private PlaceDao placeRepository;
    
    @Autowired
    public PlaceService(PlaceDao placeRepository) {
        this.placeRepository = placeRepository;
    }

    public List<Place> findAllPlaces() {
        return placeRepository.findAll(); 
    }

}

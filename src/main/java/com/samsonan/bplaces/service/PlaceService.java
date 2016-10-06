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

    public Place findPlaceById(int id) {
        return placeRepository.findById(id);
    }

    public void savePlace(Place place) {

        if (place.getId() != null) {
            placeRepository.update(place);        
        } else {
            placeRepository.save(place);        
        }
    }

    public void deletePlaceById(int id) {
        placeRepository.deleteById(id);        
    }

    public void deletePlace(Place place) {
        placeRepository.delete(place);        
    }
    
    
}

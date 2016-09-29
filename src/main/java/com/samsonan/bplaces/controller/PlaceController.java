package com.samsonan.bplaces.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.samsonan.bplaces.model.Place;
import com.samsonan.bplaces.service.PlaceService;

@Controller
public class PlaceController {

    private PlaceService placeService;
    
    @Autowired
    public PlaceController(PlaceService placeService) {
        this.placeService = placeService;
    }

    //-------------------Retrieve All Places--------------------------------------------------------
    @RequestMapping(value = "/places/", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<List<Place>> findAllPlaces() {
        List<Place> places = placeService.findAllPlaces();
        if(places.isEmpty()){
            return new ResponseEntity<List<Place>>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<List<Place>>(places, HttpStatus.OK);
    }    
    
}

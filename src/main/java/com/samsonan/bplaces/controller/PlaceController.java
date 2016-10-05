package com.samsonan.bplaces.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.samsonan.bplaces.exception.ResourceNotFoundException;
import com.samsonan.bplaces.model.Place;
import com.samsonan.bplaces.service.PlaceService;

@Controller
public class PlaceController {

    public static final String FLASH_CSS_ATTR = "css"; 
    public static final String FLASH_MSG_ATTR = "msg"; 

    public static final String FLASH_CSS_VALUE_OK = "success"; 
    public static final String FLASH_CSS_VALUE_ERROR = "danger"; 
    
    private PlaceService placeService;
    
    @Autowired
    public PlaceController(PlaceService placeService) {
        this.placeService = placeService;
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleResourceNotFoundException() {
        return "errors/404";
    }   
    
    
    //------------------- Retrieve All Places--------------------------------------------------------
    @RequestMapping(value = "/api/places/", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<List<Place>> findAllPlaces() {
        List<Place> places = placeService.findAllPlaces();
        if(places.isEmpty()){
            return new ResponseEntity<List<Place>>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<List<Place>>(places, HttpStatus.OK);
    }    

    //------------------- Retrieve Place by ID ------------------------------------------------------
    @RequestMapping(value = "/api/places/{id}", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<Place> findPlaceById(@PathVariable int id) {
        Place place = placeService.findPlaceById(id);
        if(place == null){
            return new ResponseEntity<Place>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<Place>(place, HttpStatus.OK);
    }    

    //------------------- View place(s) ----------------------------------------------------
    
    @RequestMapping(value = {"/places/list"}, method = RequestMethod.GET)
    public String listPlaces(Model model) {
        
        List<Place> places = placeService.findAllPlaces();
        model.addAttribute("places", places);
        return "places/list";
    }        

    @RequestMapping(value = {"/places/{id}/details"}, method = RequestMethod.GET)
    public String viewPlace(@PathVariable int id, Model model) {
        Place place = placeService.findPlaceById(id);
        
        if (place == null) {
            throw new ResourceNotFoundException("Place id "+id+" not found.");
        }
        
        model.addAttribute("place", place);
        return "places/details";
    }
    
    //------------------ Add new place ------------------------------------------------------
    @RequestMapping(value = {"/places/add"}, method = RequestMethod.GET)
    public String addPlace(Model model) {
        Place place = new Place();
        model.addAttribute("place", place);
        return "places/edit";
    }       

    @RequestMapping(value = {"/places/add"}, method = RequestMethod.POST)
    public String addPlace(@Valid Place place, BindingResult result) {
        
        if (savePlace(place, result) < 1)
            return "places/edit";
 
        return "redirect:list";
    }    
    
    //------------------ Edit place ------------------------------------------------------
    @RequestMapping(value = {"/places/{id}/edit"}, method = RequestMethod.GET)
    public String editPlace(@PathVariable int id, Model model, RedirectAttributes redirectAttributes) {
        Place place = placeService.findPlaceById(id);
        
        if (place == null) {
            redirectAttributes.addFlashAttribute(FLASH_CSS_ATTR, FLASH_CSS_VALUE_ERROR);
            redirectAttributes.addFlashAttribute(FLASH_MSG_ATTR, "place.not.found"); //TODO:i18n
            return "redirect:/places/list";
        }
        
        model.addAttribute("place", place);
        return "places/edit";
    }

    @RequestMapping(value = {"/places/{id}/edit"}, method = RequestMethod.POST)
    public String editPlace(@Valid Place place, BindingResult result) {
        
        if (savePlace(place, result) < 1)
            return "places/edit";
 
        return "redirect:/places/list";
    }
    
    //------------------ Delete place ------------------------------------------------------
    @RequestMapping(value = {"/places/{id}/delete"}, method = RequestMethod.GET) 
    public String deletePlace(@PathVariable int id, Model model, RedirectAttributes redirectAttributes) {
        
        Place place = placeService.findPlaceById(id);
        if (place == null) {
            redirectAttributes.addFlashAttribute(FLASH_CSS_ATTR, FLASH_CSS_VALUE_ERROR);
            redirectAttributes.addFlashAttribute(FLASH_MSG_ATTR, "place.not.found"); //TODO:i18n
        } else {
            placeService.deletePlace(place);
            redirectAttributes.addFlashAttribute(FLASH_CSS_ATTR, FLASH_CSS_VALUE_OK);
            redirectAttributes.addFlashAttribute(FLASH_MSG_ATTR, "place.delete.success"); //TODO:i18n
        }
        return "redirect:/places/list";
    }       
    
    
    private int savePlace(@Valid Place place, BindingResult result){

        boolean isError = false;
        
        if (result.hasErrors()) {
            isError = true;         
        }
       
        if (isError) return -1;
        
        placeService.savePlace(place);
        return 1;
    }
    
    
    
    
}

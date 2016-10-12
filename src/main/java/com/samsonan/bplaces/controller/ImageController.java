package com.samsonan.bplaces.controller;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.samsonan.bplaces.exception.ResourceNotFoundException;
import com.samsonan.bplaces.model.Image;
import com.samsonan.bplaces.model.ImageForm;
import com.samsonan.bplaces.model.Place;
import com.samsonan.bplaces.service.ImageService;
import com.samsonan.bplaces.service.PlaceService;
import com.samsonan.bplaces.service.StorageService;
import com.samsonan.bplaces.validation.ImageFormValidator;

@Controller
public class ImageController {

    private static Logger log = LoggerFactory.getLogger(ImageController.class);

    public static final String FLASH_CSS_ATTR = "css"; 
    public static final String FLASH_MSG_ATTR = "msg"; 

    public static final String FLASH_CSS_VALUE_OK = "success"; 
    public static final String FLASH_CSS_VALUE_ERROR = "danger"; 
    
    private StorageService storageService;
    private ImageService imageService;
    private PlaceService placeService;

    ImageFormValidator formValidator;
    
    @Autowired
    public ImageController(StorageService storageService, ImageService imageService, PlaceService placeService,
            ImageFormValidator formValidator) {
        this.storageService = storageService;
        this.imageService = imageService;
        this.placeService = placeService;
        this.formValidator = formValidator;
    }

 
    @InitBinder("imageForm")
    protected void initBinderImageForm(WebDataBinder binder) {
        binder.setValidator(formValidator);
    }

    
    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleResourceNotFoundException() {
        return "errors/404";
    }       
    
    @RequestMapping(value = { "/places/{placeId}/images/add" }, method = RequestMethod.GET)
    public String addImage(Model model) {

        ImageForm image = new ImageForm();
        model.addAttribute("imageForm", image);

        return "images/edit";
    }

    //TODO: form validator - checking empty file
    @RequestMapping(value = { "/places/{placeId}/images/add" }, method = RequestMethod.POST)
    public String addImage(@PathVariable int placeId, @Valid @ModelAttribute("imageForm") ImageForm imageForm, BindingResult result, Model model,
            RedirectAttributes redirectAttributes) {

        log.debug("Image form submitted. ImageForm: {}", imageForm);

        if (result.hasErrors()) {
            return "images/edit";
        }

        String path = null;

        Image image = new Image();

        try {

            MultipartFile file = imageForm.getFile();

            path = storageService.store(file);

            image.setContentType(file.getContentType());
            image.setCreated(new Date());
            image.setUpdated(new Date());

            image.setTitle(imageForm.getTitle());
            image.setDescription(imageForm.getDescription());

            image.setFilename(path);

            Place place = placeService.findPlaceById(placeId);// TODO: check if exists
            image.setPlace(place);

            log.debug("Image stored. {}", image);

        } catch (Exception e) {
            log.error("Error while saving the file to the storage", e);

            redirectAttributes.addFlashAttribute(FLASH_CSS_ATTR, FLASH_CSS_VALUE_ERROR);
            redirectAttributes.addFlashAttribute(FLASH_MSG_ATTR, "Cannot save the file to the storage: "+e.getMessage());
            
            return "redirect:/places/" + placeId + "/images/edit";
        }

        imageService.saveImage(image);

        model.addAttribute("placeId", placeId);

        return "redirect:list";

    }
    
    @RequestMapping(value = { "/places/{placeId}/images/{id}/edit" }, method = RequestMethod.GET)
    public String editImage(@PathVariable int placeId, @PathVariable int id, Model model) {

        Image image = imageService.findImageById(placeId, id);
        
        if (image == null) {
            throw new ResourceNotFoundException("Image id " + id + " for place id " + placeId + " not found.");
        }
        
        ImageForm imageForm = new ImageForm();
        imageForm.setDescription(image.getDescription());
        imageForm.setTitle(image.getTitle());
        imageForm.setImagePath(storageService.getFileUrl(image.getFilename()));
        
        model.addAttribute("imageForm", imageForm);
        model.addAttribute("placeId", placeId);

        return "images/edit";
    }
    
    @RequestMapping(value = { "/places/{placeId}/images/{id}/edit" }, method = RequestMethod.POST)
    public String editImage(@PathVariable int placeId, @PathVariable int id, @Valid @ModelAttribute("imageForm") ImageForm imageForm, 
            BindingResult result, Model model) {

        log.debug("Image form submitted. ImageForm: {}", imageForm);

        if (result.hasErrors()) {
            return "images/edit";
        }

        Image image = imageService.findImageById(placeId, id); 

        image.setTitle(imageForm.getTitle());
        image.setDescription(imageForm.getDescription());

        try {
            
            MultipartFile file = imageForm.getFile();
            
            //new file is uploaded - let's replace it
            if (file != null && !file.isEmpty()) {
            
                String path = storageService.store(file);
            
                image.setContentType(file.getContentType());
                image.setUpdated(new Date());
            
                image.setFilename(path);
            
                log.debug("Image stored. {}", image);
            }

        } catch (IOException e) {
            log.error("Error while saving the file to the storage", e);
        }        

        imageService.saveImage(image);
        
        log.debug("Image updated. Image: {}", image);
        
        return "redirect:/places/" + placeId + "/images/list";
    }    

    @RequestMapping(value = { "/places/{placeId}/images/list" }, method = RequestMethod.GET)
    public String listImages(@PathVariable int placeId, 
            Model model) {

        List<Image> images = imageService.findAllImagesForPlace(placeId);
        model.addAttribute("images", images);

        model.addAttribute("placeId", placeId);
        
        Map<Integer, Object> links = new HashMap<>();
        
        for (Image image : images) {
            try{
                links.put(image.getId(), storageService.getFileUrl(image.getFilename()));
            } catch(Exception e) {
                log.error("Error loading resource for image id="+image.getId(), e);
            }
        }

        model.addAttribute("links", links);
        
        return "images/list";
    }
    
    //------------------ Delete image ------------------------------------------------------
    @RequestMapping(value = {"/places/{placeId}/images/{id}/delete"}, method = RequestMethod.GET) 
    public String deleteImage(@PathVariable int placeId, @PathVariable int id, 
            Model model, RedirectAttributes redirectAttributes) {
        
        Image image = imageService.findImageById(placeId, id);
                
        if (image == null) {
            redirectAttributes.addFlashAttribute(FLASH_CSS_ATTR, FLASH_CSS_VALUE_ERROR);
            redirectAttributes.addFlashAttribute(FLASH_MSG_ATTR, "image.not.found"); //TODO:i18n
        } else {
            imageService.deleteImage(image);
            redirectAttributes.addFlashAttribute(FLASH_CSS_ATTR, FLASH_CSS_VALUE_OK);
            redirectAttributes.addFlashAttribute(FLASH_MSG_ATTR, "image.delete.success"); //TODO:i18n
        }
        return "redirect:/places/" + placeId + "/images/list";
    }       
    
    
    
}

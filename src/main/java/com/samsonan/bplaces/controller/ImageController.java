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
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;

import com.samsonan.bplaces.model.Image;
import com.samsonan.bplaces.model.ImageForm;
import com.samsonan.bplaces.service.ImageService;
import com.samsonan.bplaces.service.StorageService;

@Controller
public class ImageController {

    private static Logger log = LoggerFactory.getLogger(ImageController.class);

    private StorageService storageService;
    private ImageService imageService;

    @Autowired
    public ImageController(StorageService storageService, ImageService imageService) {
        this.storageService = storageService;
        this.imageService = imageService;
    }

    @RequestMapping(value = { "/images/add" }, method = RequestMethod.GET)
    public String editImage(Model model) {

        ImageForm image = new ImageForm();
        model.addAttribute("imageForm", image);

        return "images/edit";
    }

    @RequestMapping(value = { "/images/add" }, method = RequestMethod.POST)
    public String editImage(@ModelAttribute("imageForm") @Valid ImageForm imageForm, BindingResult result) {

        log.debug("Image form submitted. ImageForm: {}", imageForm);

        if (result.hasErrors()) {
            return "images/test";
        }

        String path = null;

        try {
            
            MultipartFile file = imageForm.getFile();
            
            path = storageService.store(file);
            
            Image image = new Image();
            image.setContentType(file.getContentType());
            image.setCreated(new Date());
            image.setUpdated(new Date());
            
            image.setTitle(imageForm.getTitle());
            image.setDescription(imageForm.getDescription());
            
            image.setFilename(path);
            
            imageService.saveImage(image);
            
            log.debug("Image stored. {}", image);

        } catch (IOException e) {
            log.error("Error while saving the file to the storage", e);
        }

        return "images/edit";

    }

    @RequestMapping(value = { "/images/list" }, method = RequestMethod.GET)
    public String listImages(Model model) {

        List<Image> images = imageService.findAllImages();
        model.addAttribute("images", images);
        
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
    
    
}

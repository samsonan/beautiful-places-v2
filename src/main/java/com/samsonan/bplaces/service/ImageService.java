package com.samsonan.bplaces.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.samsonan.bplaces.dao.ImageDao;
import com.samsonan.bplaces.model.Image;

@Service
public class ImageService {

    private ImageDao imageRepository;
    
    @Autowired
    public ImageService(ImageDao imageRepository) {
        this.imageRepository = imageRepository;
    }

    public List<Image> findAllImages() {
        return imageRepository.findAll(); 
    }

    public Image findImageById(int id) {
        return imageRepository.findById(id);
    }

    public void saveImage(Image image) {

        if (image.getId() != null) {
            imageRepository.update(image);        
        } else {
            imageRepository.save(image);        
        }
    }

    public void deleteImageById(int id) {
        imageRepository.deleteById(id);        
    }

    public void deleteImage(Image image) {
        imageRepository.delete(image);        
    }
    
    
}

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

    public List<Image> findAllImagesForPlace(int placeId) {
        return imageRepository.findAllByPlaceId(placeId); 
    }
    
    public Image findImageById(int placeId, int imageId) {
        return imageRepository.findById(placeId, imageId);
    }

    public void saveImage(Image image) {

        if (image.getId() != null) {
            imageRepository.update(image);        
        } else {
            imageRepository.save(image);        
        }
    }

    public void deleteImageById(int placeId, int imageId) {
        imageRepository.deleteById(placeId, imageId);        
    }

    public void deleteImage(Image image) {
        imageRepository.delete(image);        
    }
    
    
}

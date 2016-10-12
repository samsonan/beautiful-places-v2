package com.samsonan.bplaces.model;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.web.multipart.MultipartFile;

import com.google.common.base.MoreObjects;

public class ImageForm {
 
	private String imageUrl;
	
    private String imagePath;

	private MultipartFile file;
	
	@NotEmpty
	private String title;

	@NotEmpty
    private String description;
	
    public MultipartFile getFile() {
        return file;
    }
 
    public void setFile(MultipartFile file) {
        this.file = file;
    }
    
    public String getImagePath() {
		return imagePath;
	}

	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}

	public String getDescription() {
        return description;
    }
 
    public void setDescription(String description) {
        this.description = description;
    }

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
    
    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @Override
    public String toString(){
        return MoreObjects.toStringHelper(this)
           .add("title", title)
           .add("description", description)
           .add("imageUrl", imageUrl)
           .add("imagePath", imagePath)
           .add("file", file != null && !file.isEmpty())
           .toString();
    }   
	
    
    
}
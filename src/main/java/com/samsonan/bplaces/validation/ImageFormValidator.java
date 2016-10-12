package com.samsonan.bplaces.validation;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.samsonan.bplaces.model.ImageForm;
 
@Component
public class ImageFormValidator implements Validator {
     
    public boolean supports(Class<?> class_) {
        return ImageForm.class.isAssignableFrom(class_);
    }
 
    public void validate(Object obj, Errors errors) {
        ImageForm form = (ImageForm) obj;
        
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "title", "NotEmpty.image.title");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "description", "NotEmpty.image.description");

        boolean isExistingFile = form.getImagePath() != null && !form.getImagePath().isEmpty();
        
        if((form.getFile() == null || form.getFile().isEmpty()) && !isExistingFile){
        	errors.rejectValue("file", "missing.form.file");
        }
        
        if(form.getFile() != null && !form.getFile().isEmpty() && form.getFile().getSize() == 0) {
            errors.rejectValue("file", "empty.form.file");
        }
    }
}
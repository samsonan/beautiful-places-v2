package com.samsonan.bplaces.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Not Found")
public class ResourceNotFoundException extends RuntimeException {

	private static final long serialVersionUID = -8103368690868712103L;

	public ResourceNotFoundException(String msg){
	    super(msg);
	}
	
}

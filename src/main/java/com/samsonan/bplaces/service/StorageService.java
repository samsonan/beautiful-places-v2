package com.samsonan.bplaces.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface StorageService {

    String store(MultipartFile file) throws IOException;
    
    String getFileUrl(String fileName);
}
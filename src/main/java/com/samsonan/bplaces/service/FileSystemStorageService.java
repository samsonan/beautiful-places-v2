package com.samsonan.bplaces.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@PropertySource(value = { "classpath:application.properties" })
public class FileSystemStorageService implements StorageService {

    private final Path rootLocation;

    @Autowired
    public FileSystemStorageService(Environment environment) {
        this.rootLocation = Paths.get(environment.getRequiredProperty("storage.upload_dir"));
    }

    @Override
    public String store(MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            throw new IOException("Failed to store empty file " + file.getOriginalFilename());
        }

        String newFilenameBase = UUID.randomUUID().toString();
        Files.copy(file.getInputStream(), this.rootLocation.resolve(newFilenameBase));

        return newFilenameBase;
    }

    @Override
    public String getFileUrl(String fileName) {
        return "/images/" + fileName;
    }


}

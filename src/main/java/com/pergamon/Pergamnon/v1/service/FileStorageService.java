package com.pergamon.Pergamnon.v1.service;

import com.pergamon.Pergamnon.v1.exception.FileNotFoundException;
import com.pergamon.Pergamnon.v1.exception.FileStorageException;
import com.pergamon.Pergamnon.v1.property.FileStorageProperties;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class FileStorageService {
    private final Path fileStorageLocation;

    @Autowired
    public FileStorageService(FileStorageProperties fileStorageProperties) {
        this.fileStorageLocation = Paths.get(fileStorageProperties.getUploadDir())
                .toAbsolutePath().normalize();
        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception ex) {
            throw new FileStorageException("Could not create the directory where the uploaded files will be stored.", ex);
        }
    }

    public Map<String, String> storeFile(URL url) {
        Map<String, String> fileNames = new HashMap<>();
        String fileName = StringUtils.cleanPath(FilenameUtils.getName(url.getPath()));

        try {
            if(fileName.contains("..")) {
                throw new FileStorageException("Sorry! Filename contains invalid path sequence " + fileName);
            }

            String storedFileName = UUID.randomUUID().toString();
            fileNames.put("fileName", fileName);
            fileNames.put("storageFileName", storedFileName);

            Path targetLocation = this.fileStorageLocation.resolve(storedFileName);
            Files.copy(url.openStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            return fileNames;
        } catch (IOException ex) {
            throw new FileStorageException("Could not store file " + fileName + ". Please try again!", ex);
        }
    }

    public void deleteFile(String storedFileName) {
        try {
            Files.delete(fileStorageLocation.resolve(storedFileName));
        } catch (IOException ex) {
            throw new FileStorageException("Could not delete file", ex);
        }
    }

    public Resource loadFileAsResource(String fileName) {
        try {
            Path filePath = this.fileStorageLocation.resolve(fileName).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            if(resource.exists()) {
                return resource;
            } else {
                throw new FileNotFoundException("File not found " + fileName);
            }
        } catch (MalformedURLException ex) {
            throw new FileNotFoundException("File not found " + fileName, ex);
        }
    }
}

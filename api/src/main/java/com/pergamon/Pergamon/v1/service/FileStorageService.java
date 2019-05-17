package com.pergamon.Pergamon.v1.service;

import com.pergamon.Pergamon.v1.entity.File;
import com.pergamon.Pergamon.v1.entity.FilePropertiesPojo;
import com.pergamon.Pergamon.v1.exception.FileNotFoundException;
import com.pergamon.Pergamon.v1.exception.FileStorageException;
import com.pergamon.Pergamon.v1.property.FileStorageProperties;
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
import java.util.UUID;

@Service
public class FileStorageService {
    private final Path fileStorageLocation;

    @Autowired
    public FileStorageService(FileStorageProperties fileStorageProperties) {
        fileStorageLocation = Paths.get(fileStorageProperties.getUploadDir())
                .toAbsolutePath().normalize();
        try {
            Files.createDirectories(fileStorageLocation);
        } catch (Exception ex) {
            throw new IllegalArgumentException("Could not create the directory where the uploaded files will be stored.", ex);
        }
    }

    public FilePropertiesPojo storeFile(URL url) {
        FilePropertiesPojo filePropertiesPojo = new FilePropertiesPojo();
        String fileName = StringUtils.cleanPath(FilenameUtils.getName(url.getPath()));
        String storedFileName = UUID.randomUUID().toString();

        try {
            if(fileName.contains("..")) {
                throw new FileStorageException("Sorry! Filename contains invalid path sequence " + fileName);
            }

            filePropertiesPojo
                    .setName(fileName)
                    .setStorageName(storedFileName)
                    .setType(url.openConnection().getContentType());

            Path targetLocation = fileStorageLocation.resolve(storedFileName);
            Files.copy(url.openStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            return filePropertiesPojo;
        } catch (IOException ex) {
            throw new FileStorageException("Could not store file " + fileName + ". Please try again!", ex);
        }
    }

    public File updateFile(URL url, File file) throws IOException {
        Path targetLocation = fileStorageLocation.resolve(file.getStorageName());
        String fileName = StringUtils.cleanPath(FilenameUtils.getName(url.getPath()));

        try {
            Files.copy(url.openStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException ex) {
            throw new FileStorageException("An error has occurred during file update!", ex);
        }

        return file.setName(fileName)
            .setType(url.openConnection().getContentType());
    }

    public Resource loadFileAsResource(String fileName) {
        try {
            Path filePath = fileStorageLocation.resolve(fileName).normalize();
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

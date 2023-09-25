package com.pergamon.Pergamon.v1.service;

import com.pergamon.Pergamon.v1.domain.FileEntity;
import com.pergamon.Pergamon.v1.domain.FileNotFoundException;
import com.pergamon.Pergamon.v1.domain.FileStorageException;
import org.apache.commons.io.FilenameUtils;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.UUID;

class FileStorageService {
    private final Path fileStorageLocation;

    public FileStorageService(String uploadDirPath) {
        fileStorageLocation = Paths.get(uploadDirPath)
                .toAbsolutePath().normalize();
        try {
            Files.createDirectories(fileStorageLocation);
        } catch (Exception ex) {
            throw new IllegalArgumentException("Could not create the directory where the uploaded files will be stored.", ex);
        }
    }

    public FileEntity storeFile(URL url) {
        String storedFileName = UUID.randomUUID().toString();


        String fileName = StringUtils.cleanPath(FilenameUtils.getName(url.getPath()));
        if(fileName.contains("..")) {
            throw new FileStorageException("Sorry! Filename contains invalid path sequence " + fileName);
        }
        try {
            String contentType = url.openConnection().getContentType();

            FileEntity fileEntity = FileEntity.builder()
                    .name(fileName)
                    .storageName(storedFileName)
                    .type(contentType)
                    .createdAt(LocalDateTime.now())
                    .build();

            Path targetLocation = fileStorageLocation.resolve(storedFileName);
            Files.copy(url.openStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            return fileEntity;
        } catch (IOException ex) {
            throw new FileStorageException("Could not store file " + fileName + ". Please try again!", ex);
        }
    }

    public FileEntity updateFile(URL url, FileEntity file) throws IOException {
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

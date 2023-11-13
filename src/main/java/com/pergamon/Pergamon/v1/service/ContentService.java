package com.pergamon.Pergamon.v1.service;

import com.pergamon.Pergamon.v1.dataaccess.ContentEntity;
import com.pergamon.Pergamon.v1.domain.ContentCommand;
import com.pergamon.Pergamon.v1.domain.ContentDomainException;
import com.pergamon.Pergamon.v1.domain.FileNotFoundException;
import com.pergamon.Pergamon.v1.domain.FileStorageException;
import lombok.extern.slf4j.Slf4j;
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
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
class ContentService {
    private final Path fileStorageLocation;

    public ContentService(String uploadDirPath) {
        fileStorageLocation = Paths.get(uploadDirPath)
                .toAbsolutePath().normalize();
        try {
            Files.createDirectories(fileStorageLocation);
        } catch (Exception ex) {
            throw new IllegalArgumentException("Could not create the directory where the uploaded files will be stored.", ex);
        }
    }

    public Optional<ValidationError> validateInitialContent(URL url) {
        String fileName = StringUtils.cleanPath(FilenameUtils.getName(url.getPath()));
        List<String> failureMessages = new ArrayList<>();
        if(fileName.contains("..")) {
            log.error("Filename contains invalid path sequence '{}", fileName);
            failureMessages.add("Filename contains invalid path sequence");
        }
        if (failureMessages.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(
                ValidationError.builder()
                        .failureMessages(failureMessages)
                        .build()
        );
    }

    public ContentCommand createContentCommand(URL url) {
        String storedFileName = UUID.randomUUID().toString();

        String fileName = StringUtils.cleanPath(FilenameUtils.getName(url.getPath()));
        String contentType = null;
        try {
            contentType = url.openConnection().getContentType();
        } catch (IOException e) {
            log.error("Could not extract contentType from url", e);
            throw new ContentDomainException("Problem while saving content");
        }
        return ContentCommand.builder()
                .name(fileName)
                .storageName(storedFileName)
                .type(contentType)
                .build();
    }
    public ContentEntity storeFile(URL url) {
        String storedFileName = UUID.randomUUID().toString();

        String fileName = StringUtils.cleanPath(FilenameUtils.getName(url.getPath()));
        try {
            String contentType = url.openConnection().getContentType();

            ContentEntity contentEntity = ContentEntity.builder()
                    .name(fileName)
                    .storageName(storedFileName)
                    .type(contentType)
                    .createdAt(OffsetDateTime.now())
                    .build();

            Path targetLocation = fileStorageLocation.resolve(storedFileName);
            Files.copy(url.openStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            return contentEntity;
        } catch (IOException ex) {
            throw new FileStorageException("Could not store file " + fileName + ". Please try again!", ex);
        }
    }

    public ContentEntity updateFile(URL url, ContentEntity file) throws IOException {
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

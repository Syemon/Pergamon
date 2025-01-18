package com.pergamon.Pergamon.v1.service;

import com.pergamon.Pergamon.v1.domain.ContentCommand;
import com.pergamon.Pergamon.v1.domain.ContentDomainException;
import com.pergamon.Pergamon.v1.domain.FileNotFoundException;
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

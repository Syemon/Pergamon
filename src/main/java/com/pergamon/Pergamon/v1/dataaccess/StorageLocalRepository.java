package com.pergamon.Pergamon.v1.dataaccess;

import com.pergamon.Pergamon.v1.domain.Content;
import com.pergamon.Pergamon.v1.domain.Resource;
import com.pergamon.Pergamon.v1.domain.ResourceStatus;
import com.pergamon.Pergamon.v1.domain.StorageRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

@Slf4j
@AllArgsConstructor
class StorageLocalRepository implements StorageRepository {

    private final Path fileStorageLocation;

    @Override
    public Resource store(Resource resource, Content content) { //TODO add try counter?
        String contentName = content.getName();
        log.info("Trying to store content '{}' from url '{}' into local repository", contentName, resource.getUrl());
        try {
            Path targetLocation = fileStorageLocation.resolve(content.getStorageName());
            Files.copy(resource.getUrl().openStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            resource.setStatus(ResourceStatus.DONE);
            log.info("Content '{}' stored successfully", contentName);
            return resource;
        } catch (IOException e) {
            log.error("Could not store file '{}'. Will try again later", contentName, e);
            resource.setStatus(ResourceStatus.ERROR);
            return resource;
        }
    }
}

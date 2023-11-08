package com.pergamon.Pergamon.v1.service;

import com.pergamon.Pergamon.v1.dataaccess.PostgresFileRepository;
import com.pergamon.Pergamon.v1.dataaccess.PostgresResourceRepository;
import com.pergamon.Pergamon.v1.domain.FileEntity;
import com.pergamon.Pergamon.v1.domain.Resource;
import com.pergamon.Pergamon.v1.domain.ResourceCommand;
import com.pergamon.Pergamon.v1.domain.ResourceCommandRepository;
import com.pergamon.Pergamon.v1.domain.ResourceEntity;
import com.pergamon.Pergamon.v1.domain.ResourceCreationException;
import com.pergamon.Pergamon.v1.domain.ResourceQueryRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Optional;

@Slf4j
@AllArgsConstructor
public class ResourceService {
    private final FileService fileService;
    private final PostgresFileRepository fileDao;
    private final PostgresResourceRepository resourceDao;
    private final ResourceCollectionModelCreator resourceCollectionModelCreator;
    private final ResourceCommandRepository resourceCommandRepository;
    private final ResourceQueryRepository resourceQueryRepository;

    @Async("threadPoolTaskExecutor") // Change to Event Listener
    @Transactional
    public void upsert(ResourceCommand resourceCommand) throws IOException {
        log.info("Trying to upsert resource from url '{}'", resourceCommand.getUrl());
        Optional<Resource> resourceOptional = resourceQueryRepository.findByUrl(resourceCommand.getUrl());
        if (resourceOptional.isPresent()) {
            log.info("Resource already exists. Resource id={}. Updating file", resourceOptional.get().getId().id());
            update(resourceCommand.getUrl()); // TODO change to send whole command
        }
        log.info("Persisting new resource and trying to download content");
        resourceCommandRepository.createResource(resourceCommand);
        create(resourceCommand);
    }

    @Transactional(rollbackFor = IOException.class)
    public void create(ResourceCommand resourceCommand) {//TODO: create before storage + set Status
        URL url = resourceCommand.getUrl();
        FileEntity fileEntity = fileService.storeFile(url);

        try {
            FileEntity file = fileDao.save(fileEntity);
            resourceDao.save(file, url);
        } catch (Exception exc) {
            throw new ResourceCreationException("There was an error during resource creation", exc);
        }
    }

    @Transactional
    public void update(URL url) throws IOException {
        FileEntity file = fileDao.findByUrl(url);
        FileEntity updateFile = fileService.updateFile(url, file);

        fileDao.update(updateFile);
    }

    @Transactional
    public boolean exists(URL url) {
        return resourceDao.exists(url);
    }

    @Transactional
    public ResponseEntity<org.springframework.core.io.Resource> download(URL url) {
        FileEntity file = fileDao.findByUrl(url);

        org.springframework.core.io.Resource fileResource =  fileService.loadFileAsResource(
                file.getStorageName());

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(file.getType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getName())
                .body(fileResource);
    }

    @Transactional
    public List<ResourceEntity> list() {
        return resourceDao.list();
    }

    @Transactional
    public List<ResourceEntity> list(String search) {
        return resourceDao.list(search);
    }

    public CollectionModel<ResourceResource> getResources(List<ResourceEntity> resources) throws MalformedURLException {
        return resourceCollectionModelCreator.getResourcesCollectionModel(resources);
    }
}

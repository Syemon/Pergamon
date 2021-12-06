package com.pergamon.Pergamon.v1.service;

import com.pergamon.Pergamon.v1.dao.PostgresFileRepository;
import com.pergamon.Pergamon.v1.dao.PostgresResourceRepository;
import com.pergamon.Pergamon.v1.entity.File;
import com.pergamon.Pergamon.v1.entity.FilePropertiesPojo;
import com.pergamon.Pergamon.v1.entity.Resource;
import com.pergamon.Pergamon.v1.exception.ResourceCreationException;
import com.pergamon.Pergamon.v1.resource.ResourceResource;
import com.pergamon.Pergamon.v1.resource.ResourceCollectionModelCreator;
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

public class ResourceService {
    private FileStorageService fileStorageService;
    private PostgresFileRepository fileDao;
    private PostgresResourceRepository resourceDao;
    private ResourceCollectionModelCreator resourceCollectionModelCreator;

    public ResourceService(
            FileStorageService fileStorageService,
            PostgresFileRepository fileDao,
            PostgresResourceRepository resourceDao,
            ResourceCollectionModelCreator resourceCollectionModelCreator
    ) {
        this.fileStorageService = fileStorageService;
        this.fileDao = fileDao;
        this.resourceDao = resourceDao;
        this.resourceCollectionModelCreator = resourceCollectionModelCreator;
    }

    @Async("threadPoolTaskExecutor")
    @Transactional
    public void upsert(URL url) throws IOException {
        if (resourceDao.exists(url)) {
            update(url);
        } else {
            create(url);
        }
    }

    @Transactional(rollbackFor = IOException.class)
    public void create(URL url) {
        FilePropertiesPojo filePropertiesPojo = fileStorageService.storeFile(url);

        try {
            File file = fileDao.save(filePropertiesPojo);
            resourceDao.save(file, url);
        } catch (Exception exc) {
            throw new ResourceCreationException("There was an error during resource creation", exc);
        }
    }

    @Transactional
    public void update(URL url) throws IOException {
        File file = fileDao.findByUrl(url);
        File updateFile = fileStorageService.updateFile(url, file);

        fileDao.update(updateFile);
    }

    @Transactional
    public boolean exists(URL url) {
        return resourceDao.exists(url);
    }

    @Transactional
    public ResponseEntity<org.springframework.core.io.Resource> download(URL url) {
        File file = fileDao.findByUrl(url);

        org.springframework.core.io.Resource fileResource =  fileStorageService.loadFileAsResource(
                file.getStorageName());

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(file.getType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getName())
                .body(fileResource);
    }

    @Transactional
    public List<Resource> list() {
        return resourceDao.list();
    }

    @Transactional
    public List<Resource> list(String search) {
        return resourceDao.list(search);
    }

    public CollectionModel<ResourceResource> getResources(List<Resource> resources) throws MalformedURLException {
        return resourceCollectionModelCreator.getResourcesCollectionModel(resources);
    }
}

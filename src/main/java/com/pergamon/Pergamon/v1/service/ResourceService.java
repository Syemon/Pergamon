package com.pergamon.Pergamon.v1.service;

import com.pergamon.Pergamon.v1.dao.PostgresFileRepository;
import com.pergamon.Pergamon.v1.dao.PostgresResourceRepository;
import com.pergamon.Pergamon.v1.domain.FileEntity;
import com.pergamon.Pergamon.v1.domain.FilePropertiesPojo;
import com.pergamon.Pergamon.v1.domain.ResourceEntity;
import com.pergamon.Pergamon.v1.exception.ResourceCreationException;
import com.pergamon.Pergamon.v1.resource.ResourceCollectionModelCreator;
import com.pergamon.Pergamon.v1.resource.ResourceResource;
import lombok.AllArgsConstructor;
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

@AllArgsConstructor
public class ResourceService {
    private final FileStorageService fileStorageService;
    private final PostgresFileRepository fileDao;
    private final PostgresResourceRepository resourceDao;
    private final ResourceCollectionModelCreator resourceCollectionModelCreator;

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
    public void create(URL url) {//TODO: create before storage + set Status
        FileEntity fileEntity = fileStorageService.storeFile(url);

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
        FileEntity updateFile = fileStorageService.updateFile(url, file);

        fileDao.update(updateFile);
    }

    @Transactional
    public boolean exists(URL url) {
        return resourceDao.exists(url);
    }

    @Transactional
    public ResponseEntity<org.springframework.core.io.Resource> download(URL url) {
        FileEntity file = fileDao.findByUrl(url);

        org.springframework.core.io.Resource fileResource =  fileStorageService.loadFileAsResource(
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

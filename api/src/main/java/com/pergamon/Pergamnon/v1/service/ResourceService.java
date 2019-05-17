package com.pergamon.Pergamnon.v1.service;

import com.pergamon.Pergamnon.v1.dao.FileDao;
import com.pergamon.Pergamnon.v1.dao.ResourceDao;
import com.pergamon.Pergamnon.v1.entity.File;
import com.pergamon.Pergamnon.v1.entity.FilePropertiesPojo;
import com.pergamon.Pergamnon.v1.entity.Resource;
import com.pergamon.Pergamnon.v1.exception.ResourceCreationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.net.URL;
import java.util.List;

@Service
public class ResourceService {
    private FileStorageService fileStorageService;
    private FileDao fileDao;
    private ResourceDao resourceDao;

    @Autowired
    public ResourceService(
            FileStorageService fileStorageService,
            FileDao fileDao,
            ResourceDao resourceDao
    ) {
        this.fileStorageService = fileStorageService;
        this.fileDao = fileDao;
        this.resourceDao = resourceDao;
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
}

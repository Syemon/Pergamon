package com.pergamon.Pergamnon.v1.service;

import com.pergamon.Pergamnon.v1.dao.FileDao;
import com.pergamon.Pergamnon.v1.dao.ResourceDao;
import com.pergamon.Pergamnon.v1.entity.File;
import com.pergamon.Pergamnon.v1.entity.FilePropertiesPojo;
import com.pergamon.Pergamnon.v1.exception.ResourceCreationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.net.URL;

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

    @Transactional(rollbackFor = IOException.class)
    public void create(URL url) throws IOException {
        FilePropertiesPojo filePropertiesPojo = this.fileStorageService.storeFile(url);

        try {
            File file = this.fileDao.save(filePropertiesPojo);
            this.resourceDao.save(file, url);
        } catch (IOException exc) {
            throw new ResourceCreationException("There was an error during resource creation", exc);
        }
    }
}

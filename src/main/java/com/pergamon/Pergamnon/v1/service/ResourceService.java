package com.pergamon.Pergamnon.v1.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.URL;
import java.util.*;

@Service
public class ResourceService {
    @Autowired
    private FileStorageService fileStorageService;

    @Transactional
    public void create(URL url) {
        Map<String, String> fileNames = this.fileStorageService.storeFile(url);
    }
}

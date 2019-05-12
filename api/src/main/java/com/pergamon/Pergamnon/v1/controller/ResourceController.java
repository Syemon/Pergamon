package com.pergamon.Pergamnon.v1.controller;

import com.pergamon.Pergamnon.v1.entity.Resource;
import com.pergamon.Pergamnon.v1.exception.ResourceNotFoundException;
import com.pergamon.Pergamnon.v1.request.ResourceRequest;
import com.pergamon.Pergamnon.v1.resource.ResourceResource;
import com.pergamon.Pergamnon.v1.resource.ResourceResourceCreator;
import com.pergamon.Pergamnon.v1.service.ResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Resources;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class ResourceController {
    private ResourceService resourceService;

    @Autowired
    public ResourceController(ResourceService resourceService) {
        this.resourceService = resourceService;
    }

    @PutMapping(value = "/resources")
    public ResponseEntity create(@RequestBody ResourceRequest resourceRequest) throws IOException, URISyntaxException {
        resourceService.create(resourceRequest.getUrl());

        return ResponseEntity.accepted().build();
    }

    @GetMapping(value = "/resources", produces = { "application/hal+json" })
    public ResponseEntity<Resources<ResourceResource>> list() throws MalformedURLException {
        List<Resource> folders = resourceService.list();

        return ResponseEntity.ok(
                ResourceResourceCreator.getResources(folders)
        );
    }

    @GetMapping(value = "/resources/downloads", produces = { "application/hal+json" })
    public ResponseEntity<org.springframework.core.io.Resource> download(@RequestParam(name = "url") URL url) {
        if (!resourceService.exists(url)) {
            throw new ResourceNotFoundException("Resource from given url doesn\'t exist");
        }

        return resourceService.download(url);
    }
}

package com.pergamon.Pergamnon.v1.controller;

import com.pergamon.Pergamnon.v1.entity.Resource;
import com.pergamon.Pergamnon.v1.request.ResourceRequest;
import com.pergamon.Pergamnon.v1.resource.ResourceResource;
import com.pergamon.Pergamnon.v1.resource.ResourceResourceCreator;
import com.pergamon.Pergamnon.v1.service.ResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Resources;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URISyntaxException;
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
    public ResponseEntity<Resources<ResourceResource>> list() {
        List<Resource> folders = resourceService.list();

        return ResponseEntity.ok(
                ResourceResourceCreator.getResources(folders)
        );
    }
}

package com.pergamon.Pergamon.v1.controller;

import com.pergamon.Pergamon.v1.entity.Resource;
import com.pergamon.Pergamon.v1.exception.ResourceConnectionException;
import com.pergamon.Pergamon.v1.exception.ResourceNotFoundException;
import com.pergamon.Pergamon.v1.request.ResourceRequest;
import com.pergamon.Pergamon.v1.resource.ResourceResource;
import com.pergamon.Pergamon.v1.resource.ResourceResourceCreator;
import com.pergamon.Pergamon.v1.service.ResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/v1")
public class ResourceController {
    private ResourceService resourceService;

    @Autowired
    public ResourceController(ResourceService resourceService) {
        this.resourceService = resourceService;
    }

    @PutMapping(value = "/resources")
    public ResponseEntity upsert(@Valid @RequestBody ResourceRequest resourceRequest) throws IOException {
        try {
            resourceRequest.getUrl().openConnection().connect();
        } catch (IOException exc) {
            throw new ResourceConnectionException("It's impossible to connect to given resource. Check given URL or try again later");
        }

        resourceService.upsert(resourceRequest.getUrl());

        return ResponseEntity.accepted().build();
    }

    @GetMapping(value = "/resources", params = {"search"}, produces = { "application/hal+json" })
    public ResponseEntity<CollectionModel<ResourceResource>> list(@RequestParam(name = "search") String search) throws MalformedURLException {
        List<Resource> folders = resourceService.list(search);

        return ResponseEntity.ok(
                ResourceResourceCreator.getResources(folders)
        );
    }

    @GetMapping(value = "/resources", produces = { "application/hal+json" })
    public ResponseEntity<CollectionModel<ResourceResource>> list() throws MalformedURLException {
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

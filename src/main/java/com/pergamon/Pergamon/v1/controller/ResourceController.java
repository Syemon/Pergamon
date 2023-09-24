package com.pergamon.Pergamon.v1.controller;

import com.pergamon.Pergamon.v1.entity.Resource;
import com.pergamon.Pergamon.v1.exception.ResourceConnectionException;
import com.pergamon.Pergamon.v1.exception.ResourceNotFoundException;
import com.pergamon.Pergamon.v1.request.ResourceRequest;
import com.pergamon.Pergamon.v1.resource.ResourceResource;
import com.pergamon.Pergamon.v1.service.ResourceService;
import jakarta.validation.Valid;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

@RestController
@RequestMapping(value = "/api/v1", produces = MediaType.APPLICATION_JSON_VALUE )
public class ResourceController {

    public final static String APPLICATION_HAL_JSON = "application/hal+json";
    private final ResourceService resourceService;

    public ResourceController(ResourceService resourceService) {
        this.resourceService = resourceService;
    }

    @PutMapping(value = "/resources")
    public ResponseEntity<Void> upsert(@Valid @RequestBody ResourceRequest resourceRequest) throws IOException {
        try {
            resourceRequest.getUrl().openConnection().connect();
        } catch (IOException exc) {
            throw new ResourceConnectionException("It's impossible to connect to given resource. Check given URL or try again later");
        }

        resourceService.upsert(resourceRequest.getUrl());

        return ResponseEntity.accepted().build();
    }

    @GetMapping(value = "/resources", params = {"search"})
    public ResponseEntity<CollectionModel<ResourceResource>> list(@RequestParam(name = "search") String search) throws MalformedURLException {
        List<Resource> folders = resourceService.list(search);

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set("Content-Type", APPLICATION_HAL_JSON);

        return ResponseEntity.ok()
                .headers(responseHeaders)
                .body(resourceService.getResources(folders));
    }

    @GetMapping(value = "/resources")
    public ResponseEntity<CollectionModel<ResourceResource>> list() throws MalformedURLException {
        List<Resource> folders = resourceService.list();

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set("Content-Type", APPLICATION_HAL_JSON);

        return ResponseEntity.ok()
                .headers(responseHeaders)
                .body(resourceService.getResources(folders));
    }

    @GetMapping(value = "/resources/downloads")
    public ResponseEntity<org.springframework.core.io.Resource> download(@RequestParam(name = "url") URL url) {
        if (!resourceService.exists(url)) {
            throw new ResourceNotFoundException("Resource from given url doesn\'t exist");
        }

        return resourceService.download(url);
    }
}

package com.pergamon.Pergamon.v1.web;

import com.pergamon.Pergamon.v1.dataaccess.ResourceEntity;
import com.pergamon.Pergamon.v1.domain.ResourceCommand;
import com.pergamon.Pergamon.v1.domain.ResourceConnectionException;
import com.pergamon.Pergamon.v1.domain.ResourceNotFoundException;
import com.pergamon.Pergamon.v1.service.ApiResponse;
import com.pergamon.Pergamon.v1.service.ResourceResource;
import com.pergamon.Pergamon.v1.service.ResourceService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

@Tag(name = "Resource", description = "Resource API")
@RestController
@RequestMapping(value = "/api/v1", produces = MediaType.APPLICATION_JSON_VALUE )
public class ResourceController {

    public final static String APPLICATION_HAL_JSON = "application/hal+json";
    private final ResourceService resourceService;

    public ResourceController(ResourceService resourceService) {
        this.resourceService = resourceService;
    }

    @PostMapping(value = "/resources")
    public ResponseEntity<ApiResponse> create(@Valid @RequestBody ResourceCommand resourceCommand) throws IOException {
        try {
            resourceCommand.getUrl().openConnection().connect();
        } catch (IOException exc) {
            throw new ResourceConnectionException("It's impossible to connect to given resource. Check given URL or try again later");
        }

        resourceService.create(resourceCommand);

        return ResponseEntity.accepted().build();
    }

    @GetMapping(value = "/resources", params = {"search"})
    public ResponseEntity<CollectionModel<ResourceResource>> list(@RequestParam(name = "search") String search) throws MalformedURLException {
        List<ResourceEntity> folders = resourceService.list(search);

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set("Content-Type", APPLICATION_HAL_JSON);

        return ResponseEntity.ok()
                .headers(responseHeaders)
                .body(resourceService.getResources(folders));
    }

    @GetMapping(value = "/resources")
    public ResponseEntity<CollectionModel<ResourceResource>> list() throws MalformedURLException {
        List<ResourceEntity> folders = resourceService.list();

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

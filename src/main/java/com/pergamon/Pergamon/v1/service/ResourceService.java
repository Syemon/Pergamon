package com.pergamon.Pergamon.v1.service;

import com.pergamon.Pergamon.v1.dataaccess.ContentEntity;
import com.pergamon.Pergamon.v1.dataaccess.PostgresFileRepository;
import com.pergamon.Pergamon.v1.dataaccess.PostgresResourceRepository;
import com.pergamon.Pergamon.v1.dataaccess.ResourceEntity;
import com.pergamon.Pergamon.v1.domain.Content;
import com.pergamon.Pergamon.v1.domain.ContentCommand;
import com.pergamon.Pergamon.v1.domain.ContentCommandRepository;
import com.pergamon.Pergamon.v1.domain.Resource;
import com.pergamon.Pergamon.v1.domain.ResourceAlreadyCreatedException;
import com.pergamon.Pergamon.v1.domain.ResourceCommand;
import com.pergamon.Pergamon.v1.domain.ResourceCommandRepository;
import com.pergamon.Pergamon.v1.domain.ResourceQueryRepository;
import com.pergamon.Pergamon.v1.domain.ResourceStatus;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.ApplicationEventMulticaster;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Optional;

@Slf4j
@AllArgsConstructor
public class ResourceService {
    private final ContentService contentService;
    private final PostgresFileRepository fileDao;
    private final PostgresResourceRepository resourceDao;
    private final ResourceCollectionModelCreator resourceCollectionModelCreator;
    private final ResourceCommandRepository resourceCommandRepository;
    private final ResourceQueryRepository resourceQueryRepository;
    private final ContentCommandRepository contentCommandRepository;
    private final ApplicationEventMulticaster applicationEventMulticaster;
    private final EventMapper eventMapper;

    public void create(ResourceCommand resourceCommand) throws IOException {
        log.info("Trying to create resource from url '{}'", resourceCommand.getUrl());
        Optional<Resource> resourceOptional = resourceQueryRepository.findByUrl(resourceCommand.getUrl());
        if (resourceOptional.isPresent()) {
            log.info("Resource already exists. Resource id={}", resourceOptional.get().getId().id());
            throw new ResourceAlreadyCreatedException("Resource " + resourceCommand.getUrl() + " was already created");
        }
        log.info("Persisting new resource and trying to download content");
        Resource resource = resourceCommandRepository.createResource(resourceCommand);
        create(resource);
    }

    public void create(Resource resource) {
        URL url = resource.getUrl();
        Optional<ValidationError> error = contentService.validateInitialContent(url);
        if (error.isPresent()) {
            log.error("Resource content failed initial validation: {}", error.get().getFailureMessages());
            resource.setStatus(ResourceStatus.FAILED);
            resourceCommandRepository.saveResource(resource);
            throw new IllegalArgumentException("Received content is not valid. Details: " + error.get().getFailureMessages());
        }
        ContentCommand contentCommand = contentService.createContentCommand(url);
        Content content = contentCommandRepository.createContent(contentCommand);
        log.info("Created content: {}", content);
        resource.setContentId(content.getId());
        resourceCommandRepository.saveResource(resource);
        log.info("Sending StoreResourceEvent");
        applicationEventMulticaster.multicastEvent(
                eventMapper.mapResourceAndContentToStoreResourceEvent(resource, content)
        );
    }

    public boolean exists(URL url) {
        return resourceQueryRepository.findByUrl(url).isPresent();
    }

    public ResponseEntity<org.springframework.core.io.Resource> download(URL url) {
        ContentEntity file = fileDao.findByUrl(url);

        org.springframework.core.io.Resource fileResource =  contentService.loadFileAsResource(
                file.getStorageName());

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(file.getType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getName())
                .body(fileResource);
    }

    public List<ResourceEntity> list() {
        return resourceDao.list();
    }

    public List<ResourceEntity> list(String search) {
        return resourceDao.list(search);
    }

    public CollectionModel<ResourceResource> getResources(List<ResourceEntity> resources) throws MalformedURLException {
        return resourceCollectionModelCreator.getResourcesCollectionModel(resources);
    }
}

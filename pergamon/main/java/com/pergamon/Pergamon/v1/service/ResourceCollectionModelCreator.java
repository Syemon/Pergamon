package com.pergamon.Pergamon.v1.service;

import com.pergamon.Pergamon.v1.dataaccess.PostgresFileRepository;
import com.pergamon.Pergamon.v1.dataaccess.ContentEntity;
import com.pergamon.Pergamon.v1.dataaccess.ResourceEntity;
import com.pergamon.Pergamon.v1.domain.ContentId;
import org.springframework.hateoas.CollectionModel;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

class ResourceCollectionModelCreator {

    private final PostgresFileRepository postgresFileRepository;

    public ResourceCollectionModelCreator(PostgresFileRepository postgresFileRepository) {
        this.postgresFileRepository = postgresFileRepository;
    }

    public CollectionModel<ResourceResource> getResourcesCollectionModel(List<ResourceEntity> resources) throws MalformedURLException {
        List<ResourceResource> folderResources = new ArrayList<>();

        for (ResourceEntity resource : resources) {
            ResourceBody body = new ResourceBody();
            Optional<ContentEntity> file = postgresFileRepository.findById(new ContentId(resource.getFileId()));

            if (file.isPresent()) {
                ContentEntity contentEntity = file.get();
                body.setUrl(resource.getUrl())
                        .setFileName(contentEntity.getName())
                        .setFileType(contentEntity.getType())
                        .setCreatedAt(contentEntity.getCreatedAt());
            }


            folderResources.add(new ResourceResource(resource, body));
        }

        return CollectionModel.of(folderResources);
    }
}

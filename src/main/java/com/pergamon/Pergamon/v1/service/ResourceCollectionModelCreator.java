package com.pergamon.Pergamon.v1.service;

import com.pergamon.Pergamon.v1.dataaccess.PostgresFileRepository;
import com.pergamon.Pergamon.v1.dataaccess.FileEntity;
import com.pergamon.Pergamon.v1.dataaccess.ResourceEntity;
import com.pergamon.Pergamon.v1.domain.FileId;
import org.springframework.hateoas.CollectionModel;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

class ResourceCollectionModelCreator {

    private final PostgresFileRepository postgresFileRepository;

    public ResourceCollectionModelCreator(PostgresFileRepository postgresFileRepository) {
        this.postgresFileRepository = postgresFileRepository;
    }

    public CollectionModel<ResourceResource> getResourcesCollectionModel(List<ResourceEntity> resources) throws MalformedURLException {
        List<ResourceResource> folderResources = new ArrayList<>();

        for (ResourceEntity resource : resources) {
            ResourceBody body = new ResourceBody();
            FileEntity file = postgresFileRepository.findById(new FileId(resource.getFileId())).get();

            body.setUrl(resource.getUrl())
                .setFileName(file.getName())
                .setFileType(file.getType())
                .setCreatedAt(file.getCreatedAt());

            folderResources.add(new ResourceResource(resource, body));
        }

        return CollectionModel.of(folderResources);
    }
}

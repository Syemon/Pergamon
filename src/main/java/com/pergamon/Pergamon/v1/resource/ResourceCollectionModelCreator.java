package com.pergamon.Pergamon.v1.resource;

import com.pergamon.Pergamon.v1.dao.PostgresFileRepository;
import com.pergamon.Pergamon.v1.domain.FileEntity;
import com.pergamon.Pergamon.v1.domain.ResourceEntity;
import org.springframework.hateoas.CollectionModel;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

public class ResourceCollectionModelCreator {

    private final PostgresFileRepository postgresFileRepository;

    public ResourceCollectionModelCreator(PostgresFileRepository postgresFileRepository) {
        this.postgresFileRepository = postgresFileRepository;
    }

    public CollectionModel<ResourceResource> getResourcesCollectionModel(List<ResourceEntity> resources) throws MalformedURLException {
        List<ResourceResource> folderResources = new ArrayList<>();

        for (ResourceEntity resource : resources) {
            ResourceBody body = new ResourceBody();
            FileEntity file = postgresFileRepository.findById(resource.getFileId()).get();

            body.setUrl(resource.getUrl())
                .setFileName(file.getName())
                .setFileType(file.getType())
                .setCreatedAt(file.getCreatedAt());

            folderResources.add(new ResourceResource(resource, body));
        }

        return CollectionModel.of(folderResources);
    }
}

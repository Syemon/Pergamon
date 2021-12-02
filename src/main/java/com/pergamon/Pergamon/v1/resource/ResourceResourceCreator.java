package com.pergamon.Pergamon.v1.resource;

import com.pergamon.Pergamon.v1.entity.File;
import com.pergamon.Pergamon.v1.entity.Resource;
import org.springframework.hateoas.CollectionModel;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

public class ResourceResourceCreator {
    public static CollectionModel<ResourceResource> getResources(List<Resource> resources) throws MalformedURLException {
        List<ResourceResource> folderResources = new ArrayList<>();

        for (Resource resource : resources) {
            ResourceBody body = new ResourceBody();
            File file = resource.getFile();

            body.setUrl(resource.getUrl())
                .setFileName(file.getName())
                .setFileType(file.getType())
                .setCreatedAt(file.getCreatedAt());

            folderResources.add(new ResourceResource(resource, body));
        }

        return new CollectionModel<>(folderResources);
    }
}

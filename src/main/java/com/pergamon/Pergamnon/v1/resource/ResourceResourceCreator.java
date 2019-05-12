package com.pergamon.Pergamnon.v1.resource;

import com.pergamon.Pergamnon.v1.entity.File;
import com.pergamon.Pergamnon.v1.entity.Resource;
import org.springframework.hateoas.Resources;

import java.util.ArrayList;
import java.util.List;

public class ResourceResourceCreator {
    public static Resources<ResourceResource> getResources(List<Resource> resources) {
        List<ResourceResource> folderResources = new ArrayList<>();

        for (Resource resource : resources) {
            ResourceBody body = new ResourceBody();
            File file = resource.getFile();

            body.setUrl(resource.getUrl())
                .setFileName(file.getName())
                .setFileType(file.getType())
                .setCreatedAt(file.getCreatedAt());

            folderResources.add(new ResourceResource(body));
        }

        return new Resources<>(folderResources);
    }
}

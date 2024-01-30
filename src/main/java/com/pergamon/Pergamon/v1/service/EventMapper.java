package com.pergamon.Pergamon.v1.service;

import com.pergamon.Pergamon.v1.domain.Content;
import com.pergamon.Pergamon.v1.domain.ContentId;
import com.pergamon.Pergamon.v1.domain.Resource;
import com.pergamon.Pergamon.v1.domain.ResourceRoot;

import java.util.Optional;

public class EventMapper {
    StoreResourceEvent mapResourceAndContentToStoreResourceEvent(Resource resource, Content content) {
        StoreResourcePayload payload = StoreResourcePayload.builder()
                .resource(resource)
                .content(content)
                .build();
        return new StoreResourceEvent(this, payload);
    }

    StoreResourceEvent mapResourceRootToStoreResourceEvent(ResourceRoot resourceRoot) {
        ContentId contentId = Optional.ofNullable(resourceRoot.getContent())
                .map(Content::getId)
                .orElse(null);

        Resource resource = new Resource()
                .setId(resourceRoot.getId())
                .setUrl(resourceRoot.getUrl())
                .setContentId(contentId)
                .setModifiedAt(resourceRoot.getModifiedAt())
                .setCreatedAt(resourceRoot.getCreatedAt())
                .setAttemptNumber(resourceRoot.getAttemptNumber())
                .setStatus(resourceRoot.getStatus());

        StoreResourcePayload payload = StoreResourcePayload.builder()
                .resource(resource)
                .content(resourceRoot.getContent())
                .build();
        return new StoreResourceEvent(this, payload);
    }
}

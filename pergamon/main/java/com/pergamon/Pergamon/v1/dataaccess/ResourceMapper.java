package com.pergamon.Pergamon.v1.dataaccess;

import com.pergamon.Pergamon.v1.domain.ContentId;
import com.pergamon.Pergamon.v1.domain.Resource;
import com.pergamon.Pergamon.v1.domain.ResourceCreationException;
import com.pergamon.Pergamon.v1.domain.ResourceId;
import lombok.extern.slf4j.Slf4j;

import java.net.MalformedURLException;
import java.net.URL;

@Slf4j
public class ResourceMapper {
    public Resource mapResourceEntityToResource(ResourceEntity resourceEntity) {
        ContentId contentId = resourceEntity.getFileId() != null ? new ContentId(resourceEntity.getFileId()) : null;
        try {
            return new Resource()
                    .setId(new ResourceId(resourceEntity.getId()))
                    .setContentId(contentId)
                    .setCreatedAt(resourceEntity.getCreatedAt())
                    .setModifiedAt(resourceEntity.getModifiedAt())
                    .setUrl(new URL(resourceEntity.getUrl()))
                    .setStatus(resourceEntity.getStatus())
                    .setAttemptNumber(resourceEntity.getAttemptNumber());
        } catch (MalformedURLException e) {
            log.error("Could not map url string to Url object", e);
            throw new ResourceCreationException("Could not map url string to Url object");
        }
    }
}

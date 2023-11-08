package com.pergamon.Pergamon.v1.dataaccess;

import com.pergamon.Pergamon.v1.domain.Resource;
import com.pergamon.Pergamon.v1.domain.ResourceEntity;

public class ResourceMapper {
    public Resource mapResourceEntityToResource(ResourceEntity resourceEntity) {
        return new Resource()
                .setId(resourceEntity.getId())
                .setFileId(resourceEntity.getFileId())
                .setCreatedAt(resourceEntity.getCreatedAt())
                .setModifiedAt(resourceEntity.getModifiedAt())
                .setUrl(resourceEntity.getUrl())
                .setStatus(resourceEntity.getStatus())
                .setAttemptNumber(resourceEntity.getAttemptNumber());
    }
}

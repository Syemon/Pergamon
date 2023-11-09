package com.pergamon.Pergamon.v1.dataaccess;

import com.pergamon.Pergamon.v1.domain.FileId;
import com.pergamon.Pergamon.v1.domain.Resource;
import com.pergamon.Pergamon.v1.domain.ResourceId;

public class ResourceMapper {
    public Resource mapResourceEntityToResource(ResourceEntity resourceEntity) {
        FileId fileId = resourceEntity.getFileId() != null ? new FileId(resourceEntity.getFileId()) : null;
        return new Resource()
                .setId(new ResourceId(resourceEntity.getId()))
                .setFileId(fileId)
                .setCreatedAt(resourceEntity.getCreatedAt())
                .setModifiedAt(resourceEntity.getModifiedAt())
                .setUrl(resourceEntity.getUrl())
                .setStatus(resourceEntity.getStatus())
                .setAttemptNumber(resourceEntity.getAttemptNumber());
    }
}

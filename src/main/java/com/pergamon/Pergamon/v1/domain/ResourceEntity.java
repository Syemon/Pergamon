package com.pergamon.Pergamon.v1.domain;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ResourceEntity {
    private ResourceId id;
    private FileId fileId;
    private String url;
    private LocalDateTime createdAt;

    public ResourceEntity(ResourceId id, FileId fileId, String url, LocalDateTime createdAt) {
        this.id = id;
        this.fileId = fileId;
        this.url = url;
        this.createdAt = createdAt;
    }

    public ResourceEntity(ResourceId id, FileId fileId, String url) {
        this.id = id;
        this.fileId = fileId;
        this.url = url;
    }

}

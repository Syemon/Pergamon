package com.pergamon.Pergamon.v1.dataaccess;

import lombok.Builder;
import lombok.Data;

import java.time.OffsetDateTime;

@Data
@Builder
public class ContentEntity {
    private Integer id;

    private String name;

    private String storageName;

    private String type;

    private OffsetDateTime createdAt;

    private OffsetDateTime updatedAt;

    public ContentEntity(Integer id, String name, String storageName, String type, OffsetDateTime createdAt, OffsetDateTime updatedAt) {
        this.id = id;
        this.name = name;
        this.storageName = storageName;
        this.type = type;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}

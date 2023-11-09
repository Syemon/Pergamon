package com.pergamon.Pergamon.v1.dataaccess;

import com.pergamon.Pergamon.v1.domain.FileId;
import lombok.Builder;
import lombok.Data;

import java.time.OffsetDateTime;

@Data
@Builder
public class FileEntity {
    private FileId id;

    private String name;

    private String storageName;

    private String type;

    private OffsetDateTime createdAt;

    private OffsetDateTime updatedAt;

    public FileEntity(FileId id, String name, String storageName, String type, OffsetDateTime createdAt, OffsetDateTime updatedAt) {
        this.id = id;
        this.name = name;
        this.storageName = storageName;
        this.type = type;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}

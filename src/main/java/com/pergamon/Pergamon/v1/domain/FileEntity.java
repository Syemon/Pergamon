package com.pergamon.Pergamon.v1.domain;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class FileEntity {
    private FileId id;

    private String name;

    private String storageName;

    private String type;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    public FileEntity(FileId id, String name, String storageName, String type, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.name = name;
        this.storageName = storageName;
        this.type = type;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public FileEntity(String name, String storageName, String type, LocalDateTime createdAt) {
        this.name = name;
        this.storageName = storageName;
        this.type = type;
        this.createdAt = createdAt;
    }
}

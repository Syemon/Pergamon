package com.pergamon.Pergamon.v1.entity;

import java.sql.Date;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class File {
    private FileId id;

    private String name;

    private String storageName;

    private String type;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    public File(FileId id, String name, String storageName, String type, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.name = name;
        this.storageName = storageName;
        this.type = type;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public FileId getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public File setName(String name) {
        this.name = name;

        return this;
    }

    public String getStorageName() {
        return storageName;
    }

    public File setStorageName(String storageName) {
        this.storageName = storageName;

        return this;
    }

    public String getType() {
        return type;
    }

    public File setType(String type) {
        this.type = type;

        return this;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}

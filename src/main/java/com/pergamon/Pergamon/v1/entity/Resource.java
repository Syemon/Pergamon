package com.pergamon.Pergamon.v1.entity;

import java.time.LocalDateTime;

public class Resource {
    private ResourceId id;
    private FileId fileId;
    private String url;
    private LocalDateTime createdAt;

    public Resource(ResourceId id, FileId fileId, String url, LocalDateTime createdAt) {
        this.id = id;
        this.fileId = fileId;
        this.url = url;
        this.createdAt = createdAt;
    }

    public Resource(ResourceId id, FileId fileId, String url) {
        this.id = id;
        this.fileId = fileId;
        this.url = url;
    }

    public ResourceId getId() {
        return id;
    }

    public FileId getFileId() {
        return fileId;
    }

    public void setFileId(FileId fileId) {
        this.fileId = fileId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Resource resource = (Resource) o;

        if (id != null ? !id.equals(resource.id) : resource.id != null) return false;
        if (fileId != null ? !fileId.equals(resource.fileId) : resource.fileId != null) return false;
        if (url != null ? !url.equals(resource.url) : resource.url != null) return false;
        return createdAt != null ? createdAt.equals(resource.createdAt) : resource.createdAt == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (fileId != null ? fileId.hashCode() : 0);
        result = 31 * result + (url != null ? url.hashCode() : 0);
        result = 31 * result + (createdAt != null ? createdAt.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Resource{" +
                "id=" + id +
                ", fileId=" + fileId +
                ", url='" + url + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}

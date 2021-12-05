package com.pergamon.Pergamon.v1.resource;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.Date;

public class ResourceBody {
    private String url;
    private String fileName;
    private String fileType;

    @JsonProperty("created_at")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime createdAt;

    public String getUrl() {
        return url;
    }

    public ResourceBody setUrl(String url) {
        this.url = url;

        return this;
    }

    public String getFileName() {
        return fileName;
    }

    public ResourceBody setFileName(String fileName) {
        this.fileName = fileName;

        return this;
    }

    public String getFileType() {
        return fileType;
    }

    public ResourceBody setFileType(String fileType) {
        this.fileType = fileType;

        return this;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public ResourceBody setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;

        return this;
    }
}

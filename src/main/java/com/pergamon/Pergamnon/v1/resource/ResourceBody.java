package com.pergamon.Pergamnon.v1.resource;

public class ResourceBody {
    private String url;
    private String fileName;

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
}

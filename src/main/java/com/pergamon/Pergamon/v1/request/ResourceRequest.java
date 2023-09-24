package com.pergamon.Pergamon.v1.request;

import jakarta.validation.constraints.NotNull;

import java.net.URL;

public class ResourceRequest {
    private URL url;

    @NotNull
    public URL getUrl() {
        return url;
    }

    public void setUrl(URL url) {
        this.url = url;
    }
}

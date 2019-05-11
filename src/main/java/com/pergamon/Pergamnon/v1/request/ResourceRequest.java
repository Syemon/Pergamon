package com.pergamon.Pergamnon.v1.request;

import javax.validation.constraints.NotNull;
import java.net.URL;

public class ResourceRequest {
    private URL url;

    @NotNull
    @org.hibernate.validator.constraints.URL
    public URL getUrl() {
        return url;
    }

    public void setUrl(URL url) {
        this.url = url;
    }
}

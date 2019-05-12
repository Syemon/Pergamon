package com.pergamon.Pergamnon.v1.resource;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.hateoas.ResourceSupport;

public class ResourceResource extends ResourceSupport {
    private ResourceBody body;

    public ResourceResource(ResourceBody body) {
        this.body = body;
    }

    @JsonProperty("resource")
    public ResourceBody getBody() {
        return body;
    }
}

package com.pergamon.Pergamnon.v1.resource;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.pergamon.Pergamnon.v1.controller.ResourceController;
import com.pergamon.Pergamnon.v1.entity.Resource;
import org.springframework.hateoas.ResourceSupport;

import java.net.MalformedURLException;
import java.net.URL;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

public class ResourceResource extends ResourceSupport {
    private ResourceBody body;

    public ResourceResource(Resource resource, ResourceBody body) throws MalformedURLException {
        add(linkTo(methodOn(ResourceController.class).download(new URL(resource.getUrl()))).withRel("download"));

        this.body = body;
    }

    @JsonProperty("resource")
    public ResourceBody getBody() {
        return body;
    }
}

package com.pergamon.Pergamon.v1.resource;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.pergamon.Pergamon.v1.controller.ResourceController;
import com.pergamon.Pergamon.v1.entity.Resource;
import org.springframework.hateoas.RepresentationModel;

import java.net.MalformedURLException;
import java.net.URL;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

public class ResourceResource extends RepresentationModel {
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

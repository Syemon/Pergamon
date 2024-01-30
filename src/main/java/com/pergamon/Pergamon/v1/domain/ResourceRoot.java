package com.pergamon.Pergamon.v1.domain;

import lombok.Data;

import java.net.URL;
import java.time.OffsetDateTime;

@Data
public class ResourceRoot {

    private ResourceId id;
    private Content content;
    private URL url;
    private OffsetDateTime createdAt;
    private OffsetDateTime modifiedAt;
    private ResourceStatus status;
    private int attemptNumber;
}

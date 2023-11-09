package com.pergamon.Pergamon.v1.domain;

import lombok.Data;

import java.time.OffsetDateTime;

@Data
public class Resource {

    private ResourceId id;
    private FileId fileId;
    private String url;
    private OffsetDateTime createdAt;
    private OffsetDateTime modifiedAt;
    private ResourceStatus status;
    private int attemptNumber;
}

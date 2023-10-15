package com.pergamon.Pergamon.v1.domain;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Resource {

    private ResourceId id;
    private FileId fileId;
    private String url;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private ResourceStatus status;
    private int attemptNumber;
}

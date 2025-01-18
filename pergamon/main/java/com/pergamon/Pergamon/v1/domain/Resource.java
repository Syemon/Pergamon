package com.pergamon.Pergamon.v1.domain;

import lombok.Data;

import java.net.URL;
import java.time.OffsetDateTime;

@Data
public class Resource {

    private ResourceId id;
    private ContentId contentId;
    private URL url;
    private OffsetDateTime createdAt;
    private OffsetDateTime modifiedAt;
    private ResourceStatus status;
    private int attemptNumber;

    public int incrementAttemptNumber() {
        this.attemptNumber++;
        return attemptNumber;
    }
}

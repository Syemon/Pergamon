package com.pergamon.Pergamon.v1.domain;

import lombok.Builder;
import lombok.Data;

import java.time.OffsetDateTime;

@Data
@Builder
public class Content {
    private ContentId id;
    private String name;
    private String storageName;
    private String type;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
}

package com.pergamon.Pergamon.v1.dataaccess;

import com.pergamon.Pergamon.v1.domain.ResourceStatus;
import lombok.Data;

import java.time.OffsetDateTime;

@Data
public class ResourceEntity {
    private Integer id;
    private Integer fileId;
    private String url;
    private ResourceStatus status;
    private OffsetDateTime createdAt;
    private OffsetDateTime modifiedAt;
    private int attemptNumber;
}

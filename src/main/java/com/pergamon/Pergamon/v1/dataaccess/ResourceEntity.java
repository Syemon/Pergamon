package com.pergamon.Pergamon.v1.dataaccess;

import com.pergamon.Pergamon.v1.domain.FileId;
import com.pergamon.Pergamon.v1.domain.ResourceId;
import com.pergamon.Pergamon.v1.domain.ResourceStatus;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ResourceEntity {
    private ResourceId id;
    private FileId fileId;
    private String url;
    private ResourceStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private int attemptNumber;

}

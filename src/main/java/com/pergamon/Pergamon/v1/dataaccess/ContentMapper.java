package com.pergamon.Pergamon.v1.dataaccess;

import com.pergamon.Pergamon.v1.domain.Content;
import com.pergamon.Pergamon.v1.domain.ContentId;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ContentMapper {
    public Content mapContentEntityToContent(ContentEntity contentEntity) {
        return Content.builder()
                .id(new ContentId(contentEntity.getId()))
                .name(contentEntity.getName())
                .storageName(contentEntity.getStorageName())
                .type(contentEntity.getType())
                .createdAt(contentEntity.getCreatedAt())
                .build();
    }
}

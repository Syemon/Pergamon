package com.pergamon.Pergamon.v1.service;

import com.pergamon.Pergamon.v1.domain.Content;
import com.pergamon.Pergamon.v1.domain.Resource;

public class EventMapper {
    StoreResourceEvent mapResourceAndContentToStoreResourceEvent(Resource resource, Content content) {
        StoreResourcePayload payload = StoreResourcePayload.builder()
                .resource(resource)
                .content(content)
                .build();
        return new StoreResourceEvent(this, payload);
    }
}

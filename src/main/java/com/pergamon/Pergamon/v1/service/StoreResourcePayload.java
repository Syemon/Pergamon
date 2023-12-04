package com.pergamon.Pergamon.v1.service;

import com.pergamon.Pergamon.v1.domain.Content;
import com.pergamon.Pergamon.v1.domain.Resource;
import lombok.Builder;

@Builder
record StoreResourcePayload(Resource resource, Content content) {
}

package com.pergamon.Pergamon.v1.service;

import com.pergamon.Pergamon.v1.domain.Resource;
import com.pergamon.Pergamon.v1.domain.ResourceCommandRepository;
import com.pergamon.Pergamon.v1.domain.ResourceStatus;
import com.pergamon.Pergamon.v1.domain.StorageRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;

@Slf4j
@AllArgsConstructor
public class StoreResourceListener {

    private final StorageRepository storageRepository;
    private final ResourceCommandRepository resourceCommandRepository;

    @EventListener
    public void store(StoreResourceEvent event) {
        log.info("Received event: '{}'", event);
        Resource resource = event.getPayload().resource();
        resource.setStatus(ResourceStatus.IN_PROGRESS);
        log.info("Starting storing resource: '{}'", resource);
        resourceCommandRepository.saveResource(resource);
        resource = storageRepository.store(resource, event.getPayload().content());
        log.info("Saving result resource: '{}'", resource);
        resourceCommandRepository.saveResource(resource);
    }
}

package com.pergamon.Pergamon.v1.service;

import com.pergamon.Pergamon.v1.domain.ResourceRoot;
import com.pergamon.Pergamon.v1.domain.ResourceRootQueryRepository;
import com.pergamon.Pergamon.v1.domain.ResourceStatus;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.ApplicationEventMulticaster;

import java.util.List;
import java.util.Set;

@Slf4j
@AllArgsConstructor
class StoreResourceSchedulerProcessor {

    private final static Set<ResourceStatus> STATUSES_TO_RETRY = Set.of(ResourceStatus.RETRY);
    private final static Integer RETRY_RESOURCES_OLDER_THEN_IN_MINUTES = 30;
    private final static Integer MAX_RETRY_COUNT = 3;

    private final ResourceRootQueryRepository resourceRootQueryRepository;
    private final ApplicationEventMulticaster applicationEventMulticaster;
    private final EventMapper eventMapper;

    public void retry() {
        List<ResourceRoot> resourceRoots = resourceRootQueryRepository.listToRetry(
                STATUSES_TO_RETRY,
                RETRY_RESOURCES_OLDER_THEN_IN_MINUTES,
                MAX_RETRY_COUNT
        );
        if (resourceRoots.isEmpty()) {
            log.info("No Resources to retry");
        }
        log.info("Found {} resources to retry", resourceRoots.size());

        resourceRoots.stream()
                .map(eventMapper::mapResourceRootToStoreResourceEvent)
                .forEach(applicationEventMulticaster::multicastEvent);
    }
}

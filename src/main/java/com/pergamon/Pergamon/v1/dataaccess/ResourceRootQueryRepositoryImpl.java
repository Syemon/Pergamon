package com.pergamon.Pergamon.v1.dataaccess;

import com.pergamon.Pergamon.v1.domain.ResourceRoot;
import com.pergamon.Pergamon.v1.domain.ResourceRootQueryRepository;
import com.pergamon.Pergamon.v1.domain.ResourceStatus;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.Set;

@AllArgsConstructor
class ResourceRootQueryRepositoryImpl implements ResourceRootQueryRepository {

    private final PostgresResourceRepository resourceRepository;

    @Override
    public List<ResourceRoot> listToRetry(Set<ResourceStatus> resourceStatuses, int olderThenInMinutes, int maxRetryCount) {
        return resourceRepository.listResourceRootToRetry(resourceStatuses, olderThenInMinutes, maxRetryCount);
    }
}

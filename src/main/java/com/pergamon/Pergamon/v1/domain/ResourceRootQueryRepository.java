package com.pergamon.Pergamon.v1.domain;

import java.util.List;
import java.util.Set;

public interface ResourceRootQueryRepository {
    List<ResourceRoot> listToRetry(Set<ResourceStatus> resourceStatuses, int olderThenInMinutes, int maxRetryCount);
}

package com.pergamon.Pergamon.v1.dataaccess;

import com.pergamon.Pergamon.v1.domain.Resource;
import com.pergamon.Pergamon.v1.domain.ResourceEntity;
import com.pergamon.Pergamon.v1.domain.ResourceQueryRepository;
import lombok.AllArgsConstructor;

import java.net.URL;
import java.util.Optional;

@AllArgsConstructor
class ResourceQueryRepositoryImpl implements ResourceQueryRepository {

    private final PostgresResourceRepository resourceRepository;
    private final ResourceMapper resourceMapper;

    @Override
    public Optional<Resource> findByUrl(URL url) {
        Optional<ResourceEntity> resourceEntity = resourceRepository.findByUrl(url.toString());
        return resourceEntity
                .flatMap(
                        entity -> Optional.ofNullable(resourceMapper.mapResourceEntityToResource(entity))
                );
    }
}

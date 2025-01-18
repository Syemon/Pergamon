package com.pergamon.Pergamon.v1.dataaccess;

import com.pergamon.Pergamon.v1.domain.Resource;
import com.pergamon.Pergamon.v1.domain.ResourceCommand;
import com.pergamon.Pergamon.v1.domain.ResourceCommandRepository;
import lombok.AllArgsConstructor;

@AllArgsConstructor
class ResourceCommandRepositoryImpl implements ResourceCommandRepository {

    private final PostgresResourceRepository resourceRepository;
    private final ResourceMapper resourceMapper;

    @Override
    public Resource createResource(ResourceCommand resourceCommand) {
        ResourceEntity resourceEntity = resourceRepository.create(resourceCommand) ;
        return resourceMapper.mapResourceEntityToResource(resourceEntity);
    }

    @Override
    public Resource saveResource(Resource resource) {
        ResourceEntity resourceEntity = resourceRepository.save(resource) ;
        return resourceMapper.mapResourceEntityToResource(resourceEntity);
    }
}

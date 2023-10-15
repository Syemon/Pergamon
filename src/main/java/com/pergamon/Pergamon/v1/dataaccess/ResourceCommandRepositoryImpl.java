package com.pergamon.Pergamon.v1.dataaccess;

import com.pergamon.Pergamon.v1.domain.Resource;
import com.pergamon.Pergamon.v1.domain.ResourceCommand;
import com.pergamon.Pergamon.v1.domain.ResourceCommandRepository;
import com.pergamon.Pergamon.v1.domain.ResourceEntity;

public class ResourceCommandRepositoryImpl implements ResourceCommandRepository {

    private final PostgresResourceRepository resourceRepository;
    private final ResourceMapper resourceMapper;

    public ResourceCommandRepositoryImpl(PostgresResourceRepository resourceRepository, ResourceMapper resourceMapper) {
        this.resourceRepository = resourceRepository;
        this.resourceMapper = resourceMapper;
    }

    @Override
    public Resource createResource(ResourceCommand resourceCommand) {
        ResourceEntity resourceEntity = resourceRepository.create(resourceCommand) ;
        return resourceMapper.mapResourceEntityToResource(resourceEntity);
    }
}

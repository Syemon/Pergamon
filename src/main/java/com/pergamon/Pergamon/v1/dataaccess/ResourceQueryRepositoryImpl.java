package com.pergamon.Pergamon.v1.dataaccess;

import com.pergamon.Pergamon.v1.domain.Resource;
import com.pergamon.Pergamon.v1.domain.ResourceQueryRepository;
import lombok.AllArgsConstructor;

import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

    @Override
    public List<Resource> list() {
        return resourceRepository.list().stream()
                .map(resourceMapper::mapResourceEntityToResource)
                .collect(Collectors.toList());
    }

    @Override
    public List<Resource> list(String urlFragment) {
        return resourceRepository.list(urlFragment).stream()
                .map(resourceMapper::mapResourceEntityToResource)
                .collect(Collectors.toList());
    }
}

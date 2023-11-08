package com.pergamon.Pergamon.v1.service;

import com.pergamon.Pergamon.v1.dataaccess.PostgresFileRepository;
import com.pergamon.Pergamon.v1.dataaccess.PostgresResourceRepository;
import com.pergamon.Pergamon.v1.domain.ResourceCommandRepository;
import com.pergamon.Pergamon.v1.domain.ResourceQueryRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

@org.springframework.context.annotation.Configuration
public class ServiceConfiguration {

    @Bean
    public FileService fileStorageService(@Value("${file.upload-dir}") String uploadDirPath) {
        return new FileService(uploadDirPath);
    }

    @Bean
    public ResourceCollectionModelCreator resourceCollectionModelCreator(PostgresFileRepository postgresFileRepository) {
        return new ResourceCollectionModelCreator(postgresFileRepository);
    }

    @Bean
    public ResourceService resourceService(
            FileService fileService,
            PostgresFileRepository postgresFileRepository,
            PostgresResourceRepository postgresResourceRepository,
            ResourceCollectionModelCreator resourceCollectionModelCreator,
            ResourceCommandRepository resourceCommandRepository,
            ResourceQueryRepository resourceQueryRepository
    ) {
        return new ResourceService(
                fileService,
                postgresFileRepository,
                postgresResourceRepository,
                resourceCollectionModelCreator,
                resourceCommandRepository,
                resourceQueryRepository
        );
    }
}

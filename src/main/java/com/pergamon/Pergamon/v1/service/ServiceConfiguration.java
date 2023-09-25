package com.pergamon.Pergamon.v1.service;

import com.pergamon.Pergamon.v1.dataaccess.PostgresFileRepository;
import com.pergamon.Pergamon.v1.dataaccess.PostgresResourceRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

@org.springframework.context.annotation.Configuration
public class ServiceConfiguration {

    @Bean
    public FileStorageService fileStorageService(@Value("${file.upload-dir}") String uploadDirPath) {
        return new FileStorageService(uploadDirPath);
    }

    @Bean
    public ResourceCollectionModelCreator resourceCollectionModelCreator(PostgresFileRepository postgresFileRepository) {
        return new ResourceCollectionModelCreator(postgresFileRepository);
    }

    @Bean
    public ResourceService resourceService(
            FileStorageService fileStorageService,
            PostgresFileRepository postgresFileRepository,
            PostgresResourceRepository postgresResourceRepository,
            ResourceCollectionModelCreator resourceCollectionModelCreator) {
        return new ResourceService(fileStorageService, postgresFileRepository, postgresResourceRepository, resourceCollectionModelCreator);
    }
}

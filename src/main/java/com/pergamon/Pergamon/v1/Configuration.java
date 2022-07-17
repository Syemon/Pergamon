package com.pergamon.Pergamon.v1;

import com.pergamon.Pergamon.v1.dao.PostgresFileRepository;
import com.pergamon.Pergamon.v1.dao.PostgresResourceRepository;
import com.pergamon.Pergamon.v1.service.FileStorageService;
import com.pergamon.Pergamon.v1.service.ResourceService;
import com.pergamon.Pergamon.v1.resource.ResourceCollectionModelCreator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;

@org.springframework.context.annotation.Configuration
public class Configuration {

    @Bean
    public PostgresResourceRepository postgresResourceRepository(JdbcTemplate jdbcTemplate) {
        return new PostgresResourceRepository(jdbcTemplate);
    }

    @Bean
    public PostgresFileRepository postgresFileRepository(JdbcTemplate jdbcTemplate) {
        return new PostgresFileRepository(jdbcTemplate);
    }

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
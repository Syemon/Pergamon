package com.pergamon.Pergamon.v1.dataaccess;

import com.pergamon.Pergamon.v1.domain.ContentCommandRepository;
import com.pergamon.Pergamon.v1.domain.ResourceCommandRepository;
import com.pergamon.Pergamon.v1.domain.ResourceQueryRepository;
import com.pergamon.Pergamon.v1.domain.ResourceRootQueryRepository;
import com.pergamon.Pergamon.v1.domain.StorageRepository;
import net.javacrumbs.shedlock.spring.annotation.EnableSchedulerLock;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@org.springframework.context.annotation.Configuration
@EnableScheduling
@EnableSchedulerLock(defaultLockAtMostFor = "PT30S")
public class DataAccessConfiguration {

    @Bean
    public PostgresResourceRepository postgresResourceRepository(JdbcTemplate jdbcTemplate) {
        return new PostgresResourceRepository(jdbcTemplate);
    }

    @Bean
    public PostgresFileRepository postgresFileRepository(JdbcTemplate jdbcTemplate) {
        return new PostgresFileRepository(jdbcTemplate);
    }

    @Bean
    public ResourceMapper resourceMapper() {
        return new ResourceMapper();
    }

    @Bean
    public ResourceCommandRepository resourceCommandRepository(PostgresResourceRepository postgresResourceRepository, ResourceMapper resourceMapper) {
        return new ResourceCommandRepositoryImpl(postgresResourceRepository, resourceMapper);
    }

    @Bean
    public ResourceQueryRepository resourceQueryRepository(PostgresResourceRepository postgresResourceRepository, ResourceMapper resourceMapper) {
        return new ResourceQueryRepositoryImpl(postgresResourceRepository, resourceMapper);
    }

    @Bean
    public ResourceRootQueryRepository resourceRootQueryRepository(PostgresResourceRepository postgresResourceRepository) {
        return new ResourceRootQueryRepositoryImpl(postgresResourceRepository);
    }

    @Bean
    public ContentMapper contentMapper() {
        return new ContentMapper();
    }

    @Bean
    public ContentCommandRepository contentCommandRepository(PostgresFileRepository postgresFileRepository, ContentMapper contentMapper) {
        return new ContentCommandRepositoryImpl(postgresFileRepository, contentMapper);
    }

    @Bean
    public StorageRepository storageRepository(@Value("${file.upload-dir}") String uploadDirPath) {
        Path fileStorageLocation = Paths.get(uploadDirPath)
                .toAbsolutePath().normalize();
        try {
            Files.createDirectories(fileStorageLocation);
            return new StorageLocalRepository(fileStorageLocation);
        } catch (Exception ex) {
            throw new IllegalArgumentException("Could not create the directory where the uploaded files will be stored.", ex);
        }
    }
}

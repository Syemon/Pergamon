package com.pergamon.Pergamon.v1.service;

import com.pergamon.Pergamon.v1.dataaccess.PostgresFileRepository;
import com.pergamon.Pergamon.v1.dataaccess.PostgresResourceRepository;
import com.pergamon.Pergamon.v1.domain.ContentCommandRepository;
import com.pergamon.Pergamon.v1.domain.ResourceCommandRepository;
import com.pergamon.Pergamon.v1.domain.ResourceQueryRepository;
import com.pergamon.Pergamon.v1.domain.ResourceRootQueryRepository;
import com.pergamon.Pergamon.v1.domain.StorageRepository;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import net.javacrumbs.shedlock.spring.annotation.EnableSchedulerLock;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.ApplicationEventMulticaster;
import org.springframework.context.event.SimpleApplicationEventMulticaster;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.scheduling.annotation.EnableScheduling;

@org.springframework.context.annotation.Configuration
@EnableScheduling
@EnableSchedulerLock(defaultLockAtMostFor = "PT30S")
public class ServiceConfiguration {

    @Bean
    public OpenAPI swaggerOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Pergamon API")
                        .description("Pergamon API Doc")
                        .version("0.0.1")
                );
    }

    @Bean
    public ContentService fileStorageService(@Value("${file.upload-dir}") String uploadDirPath) {
        return new ContentService(uploadDirPath);
    }

    @Bean
    public ResourceCollectionModelCreator resourceCollectionModelCreator(PostgresFileRepository postgresFileRepository) {
        return new ResourceCollectionModelCreator(postgresFileRepository);
    }

    @Bean
    public ResourceService resourceService(
            ContentService contentService,
            PostgresFileRepository postgresFileRepository,
            PostgresResourceRepository postgresResourceRepository,
            ResourceCollectionModelCreator resourceCollectionModelCreator,
            ResourceCommandRepository resourceCommandRepository,
            ResourceQueryRepository resourceQueryRepository,
            ContentCommandRepository contentCommandRepository,
            ApplicationEventMulticaster applicationEventMulticaster
    ) {
        return new ResourceService(
                contentService,
                postgresFileRepository,
                postgresResourceRepository,
                resourceCollectionModelCreator,
                resourceCommandRepository,
                resourceQueryRepository,
                contentCommandRepository,
                applicationEventMulticaster,
                new EventMapper()
        );
    }

    @Bean
    public ApplicationEventMulticaster applicationEventMulticaster() {
        SimpleApplicationEventMulticaster eventMulticaster =
                new SimpleApplicationEventMulticaster();

        eventMulticaster.setTaskExecutor(new SimpleAsyncTaskExecutor());
        return eventMulticaster;
    }

    @Bean
    public StoreResourceListener storeResourceListener(
            StorageRepository storageRepository,
            ResourceCommandRepository resourceCommandRepository
    ) {
        return new StoreResourceListener(storageRepository, resourceCommandRepository);
    }

    @Bean
    public StoreResourceScheduler storeResourceScheduler(
            StoreResourceSchedulerProcessor storeResourceSchedulerProcessor
    ) {
        return new StoreResourceScheduler(storeResourceSchedulerProcessor);
    }

    @Bean
    public StoreResourceSchedulerProcessor storeResourceSchedulerProcessor(
            ResourceRootQueryRepository resourceRootQueryRepository,
            ApplicationEventMulticaster applicationEventMulticaster
    ) {
        return new StoreResourceSchedulerProcessor(
                resourceRootQueryRepository,
                applicationEventMulticaster,
                new EventMapper()
        );
    }
}

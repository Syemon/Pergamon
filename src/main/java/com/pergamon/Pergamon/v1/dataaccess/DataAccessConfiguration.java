package com.pergamon.Pergamon.v1.dataaccess;

import com.pergamon.Pergamon.v1.domain.ResourceCommandRepository;
import com.pergamon.Pergamon.v1.domain.ResourceQueryRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;

@org.springframework.context.annotation.Configuration
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
}

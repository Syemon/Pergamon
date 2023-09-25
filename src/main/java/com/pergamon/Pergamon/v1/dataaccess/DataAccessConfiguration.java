package com.pergamon.Pergamon.v1.dataaccess;

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
}

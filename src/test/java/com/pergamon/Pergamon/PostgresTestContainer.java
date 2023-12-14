package com.pergamon.Pergamon;

import lombok.extern.slf4j.Slf4j;
import org.testcontainers.containers.PostgreSQLContainer;

@Slf4j
public class PostgresTestContainer extends PostgreSQLContainer<PostgresTestContainer> {

    private static final String IMAGE_VERSION = "postgres:15-alpine";

    private static PostgresTestContainer container;

    public PostgresTestContainer() {
        super(IMAGE_VERSION);
    }

    public static PostgresTestContainer getInstance() {
        if (container == null) {
            container = new PostgresTestContainer();
        }

        return container;
    }

    @Override
    public void start() {
        super.start();
        System.setProperty("DATABASE_URL", container.getJdbcUrl());
        System.setProperty("DATABASE_USER", container.getUsername());
        System.setProperty("DATABASE_PASSWORD", container.getPassword());
        log.info("Test DB conf: DATABASE_URL: {}, DATABASE_USER: {}, DATABASE_PASSWORD: {}",
                container.getJdbcUrl(), container.getUsername(), container.getPassword());
    }

    @Override
    public void stop() {
        //do nothing, JVM handles shut down
    }
}

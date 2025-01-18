package com.pergamon.Pergamon;

import org.junit.ClassRule;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.jdbc.JdbcDatabaseDelegate;

@ContextConfiguration(initializers = PostgresTestContainerResourceTest.Initializer.class)
public abstract class PostgresTestContainerResourceTest {

    @ClassRule
    public static PostgresTestContainer postgresTestContainer = PostgresTestContainer.getInstance();

    public static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
        @Override
        public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
            if (!postgresTestContainer.isRunning()) {
                postgresTestContainer.start();

                JdbcDatabaseDelegate delegate = new JdbcDatabaseDelegate(postgresTestContainer, "");
            }
        }
    }
}

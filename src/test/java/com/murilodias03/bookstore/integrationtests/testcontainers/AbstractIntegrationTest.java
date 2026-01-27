package com.murilodias03.bookstore.integrationtests.testcontainers;

import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.lifecycle.Startables;

import java.util.Map;
import java.util.stream.Stream;

@ContextConfiguration(initializers = AbstractIntegrationTest.Initializer.class)
public class AbstractIntegrationTest {
    
    static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

        static PostgreSQLContainer<?> postgresSQL = new PostgreSQLContainer<>("postgres:17-alpine");

        private static void startContainers() {
            Startables.deepStart(Stream.of(postgresSQL)).join();
        }

        private static Map<String, String> createConnectionConfiguration() {
            return Map.of(
                    "spring.datasource.url", postgresSQL.getJdbcUrl(),
                    "spring.datasource.username", postgresSQL.getUsername(),
                    "spring.datasource.password", postgresSQL.getPassword()
                    );
        }

        @Override
        public void initialize(ConfigurableApplicationContext applicationContext) {
            startContainers();

            ConfigurableEnvironment environment = applicationContext.getEnvironment();

            MapPropertySource testcontainers = new MapPropertySource("testcontainers",
                    (Map) createConnectionConfiguration());

            environment.getPropertySources().addFirst(testcontainers);
        }
    }
}
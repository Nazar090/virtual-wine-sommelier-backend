package com.example.virtualwinesommelierbackend.config;

import org.testcontainers.containers.PostgreSQLContainer;

public class CustomPostgresContainer extends PostgreSQLContainer<CustomPostgresContainer> {
    private static final String DB_IMAGE = "postgres:15-alpine";
    private static CustomPostgresContainer instance;

    private CustomPostgresContainer() {
        super(DB_IMAGE);
        this.withDatabaseName("testdb")
                .withUsername("test")
                .withPassword("test");
    }

    public static CustomPostgresContainer getInstance() {
        if (instance == null) {
            instance = new CustomPostgresContainer();
            instance.start();
        }
        return instance;
    }

    @Override
    public void stop() {
        // Override stop to prevent shutting down the container between tests
    }
}

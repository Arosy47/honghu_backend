package com.fmx.xiaomeng.common.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import redis.embedded.RedisServer;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.IOException;

/**
 * Embedded Redis configuration for local development.
 * Starts an embedded Redis server on port 6379 when the 'dev' profile is active.
 */
@Configuration
@Profile("dev")
public class EmbeddedRedisConfig {

    private RedisServer redisServer;

    @PostConstruct
    public void startRedis() {
        try {
            System.out.println("Attempting to start Embedded Redis on port 6379...");
            // Check if port 6379 is already in use or just try to start
            redisServer = RedisServer.builder()
                    .port(6379)
                    .setting("maxmemory 128M") // Fix for Windows heap issue
                    .build();
            redisServer.start();
            System.out.println("Embedded Redis started successfully on port 6379");
        } catch (Exception e) {
            System.err.println("Failed to start Embedded Redis on port 6379. " +
                    "This might be because a Redis instance is already running or port is occupied. " +
                    "Error: " + e.getMessage());
            e.printStackTrace();
            // We don't throw exception here to allow application to proceed if a real Redis is running
        }
    }

    @PreDestroy
    public void stopRedis() {
        if (redisServer != null) {
            try {
                redisServer.stop();
                System.out.println("Embedded Redis stopped");
            } catch (Exception e) {
                System.err.println("Error stopping Embedded Redis: " + e.getMessage());
            }
        }
    }
}

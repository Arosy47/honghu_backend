package com.fmx.xiaomeng.common.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;

@Configuration
public class RedissonConfig {

    @Autowired(required = false)
    private EmbeddedRedisConfig embeddedRedisConfig;

    @Bean
    public RedissonClient redissonClient() throws IOException {
        // Try to load redisson-dev.yaml (for dev profile) or fallback to redisson.yaml
        ClassPathResource resource = new ClassPathResource("redisson-dev.yaml");
        if (!resource.exists()) {
            resource = new ClassPathResource("redisson.yaml");
        }
        
        Config config = Config.fromYAML(resource.getInputStream());
        return Redisson.create(config);
    }
}

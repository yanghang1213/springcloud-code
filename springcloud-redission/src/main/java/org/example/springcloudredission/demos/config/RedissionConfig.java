package org.example.springcloudredission.demos.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RedissionConfig {
    @Bean
    public RedissonClient getRedisClient(){
        Config config = new Config();
        config.useSentinelServers().addSentinelAddress("redis://127.0.0.1:6379").setPassword("123456");
        return Redisson.create(config);
    }
}

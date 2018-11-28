package com.colossus.redis.config;


import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;

import javax.annotation.Resource;

@Configuration
@ConditionalOnClass(RedisClusterConfig.class)
@EnableConfigurationProperties(RedisClusterProperty.class)
public class RedisClusterConfig {

    @Resource
    private RedisClusterProperty redisClusterProperty;

    @Bean
    public RedisConnectionFactory connectionFactory() {

        return new JedisConnectionFactory(
                new RedisClusterConfiguration(redisClusterProperty.getNodes()));
    }
}

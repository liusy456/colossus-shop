package com.colossus.redis.service.impl;


import com.colossus.redis.service.RedisService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.CacheManager;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;
import java.util.concurrent.TimeUnit;


@Api(value = "API - RedisServiceImpl", description = "redis 服务")
@RestController
@RefreshScope
public class RedisServiceImpl implements RedisService {
    @Autowired
    @Qualifier("redisTemplate")
    private RedisTemplate redisTemplate;
    @Autowired
    private RedisCacheManager cacheManager;

    @Override
    public String get(String key) {
        return  (String) redisTemplate.opsForValue().get(key);
    }

    @Override
    public String set(String key, String value) {
        redisTemplate.opsForValue().set(key, value);
        return value;
    }

    @Override
    public String hget(String hkey, String key) {
        return (String) redisTemplate.opsForHash().get(hkey, key);
    }

    @Override
    public void hset(String hkey, String key, String value) {
         redisTemplate.opsForHash().put(hkey,key ,value );
    }

    @Override
    public long incr(String key,long amount) {
        return redisTemplate.opsForValue().increment(key, amount);
    }

    @Override
    public void expire(String key, Integer second) {
        redisTemplate.expire(key,second, TimeUnit.SECONDS);
    }

    @Override
    public long ttl(String key) {
        return redisTemplate.getExpire(key);
    }

    @Override
    public void del(String key) {
         redisTemplate.delete(key);
    }

    @Override
    public long hdel(String hkey, String key) {
        return redisTemplate.opsForHash().delete(hkey,key);
    }

    public CacheManager getCacheManager() {
        return cacheManager;
    }

    @Override
    public Set<String> keys(String hkey) {
        return redisTemplate.opsForHash().keys(hkey);
    }

    public long hlen(String hkey){
        return redisTemplate.opsForHash().size(hkey);
    }
}

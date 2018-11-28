package com.colossus.redis.service.histrix;

import com.colossus.redis.service.RedisService;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class RedisServiceHystrix implements RedisService {

    @Override
    public String get(String key) {
        return null;
    }

    @Override
    public String set(String key, String value) {
        return null;
    }

    @Override
    public String hget(String hkey, String key) {
        return null;
    }

    @Override
    public void hset(String hkey, String key, String value) {

    }

    @Override
    public long incr(String key, long amout) {
        return 0;
    }

    @Override
    public void expire(String key, Integer second) {

    }

    @Override
    public long ttl(String key) {
        return 0;
    }

    @Override
    public void del(String key) {

    }

    @Override
    public long hdel(String hkey, String key) {
        return 0;
    }

    @Override
    public CacheManager getCacheManager() {
        return null;
    }

    @Override
    public Set<String> keys(String hkey) {
        return null;
    }

    @Override
    public long hlen(String hkey) {
        return 0;
    }
}

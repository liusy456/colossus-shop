package com.colossus.auth.shiro;

import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.apache.shiro.cache.CacheManager;

public class RedisCacheManager implements CacheManager {

   private RedisCache redisCache;
   public RedisCacheManager(RedisCache redisCache){
       this.redisCache=redisCache;
   }
    @Override
    public <K, V> Cache<K, V> getCache(String s) throws CacheException {
        return redisCache;
    }
}

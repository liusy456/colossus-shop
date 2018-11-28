package com.colossus.auth.shiro;

import com.alibaba.fastjson.JSON;
import com.colossus.redis.service.RedisService;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public class RedisCache<K,V> implements Cache<K,V> {

   private RedisService redisService;

   private String shiroKeys;
   public RedisCache(String shiroKeys,RedisService redisService){
        this.shiroKeys=shiroKeys;
        this.redisService=redisService;
    }
    @Override
    public V get(K k) throws CacheException {

        return (V) JSON.parse(redisService.hget(shiroKeys,JSON.toJSONString(k)));
    }

    @Override
    public V put(K k, V v) throws CacheException {
        redisService.hset(shiroKeys,JSON.toJSONString(k),JSON.toJSONString(k));
        return v;
    }

    @Override
    public V remove(K k) throws CacheException {
       V v=get(k);
       redisService.hdel(shiroKeys,JSON.toJSONString(k));
       return v;
    }

    @Override
    public void clear() throws CacheException {

        Set<String> keys=redisService.keys(shiroKeys);
        for (String key : keys) {
            redisService.hdel(shiroKeys,key);
        }
    }

    @Override
    public int size() {
        return (int) redisService.hlen(shiroKeys);
    }

    @Override
    public Set<K> keys() {
       Set<K> result=Sets.newHashSet();
        for (String s : redisService.keys(shiroKeys)) {
            result.add((K) s);
        }
        return result;
    }

    @Override
    public Collection<V> values() {
        List<V> result= Lists.newArrayList();
        for (String s : redisService.keys(shiroKeys)) {
            result.add((V)JSON.parse(redisService.hget(shiroKeys,s)));
        }
        return result;
    }
}

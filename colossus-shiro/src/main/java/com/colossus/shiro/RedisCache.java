package com.colossus.shiro;

import com.alibaba.fastjson.JSON;
import com.colossus.common.utils.SerializableUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.*;
import java.util.concurrent.TimeUnit;

public class RedisCache<K,V> implements Cache<K,V> {

    private RedisTemplate redisTemplate;

    private String shiroKeys;
    private long expire;
    public RedisCache(String shiroKeys, RedisTemplate redisTemplate, long expire){
        if (redisTemplate == null) {
            throw new IllegalArgumentException("Cache argument cannot be null.");
        }
        this.shiroKeys=shiroKeys;
        this.redisTemplate=redisTemplate;
        this.expire=expire;
    }
    private Logger logger = LoggerFactory.getLogger(this.getClass());



    /**
     * 获得byte[]型的key
     * @param key
     * @return
     */
    private String  getStringKey(K key){
        if(key instanceof String){
            return this.shiroKeys + key;
        }else{
            return JSON.toJSONString(key);
        }
    }

    @Override
    public V get(K key) throws CacheException {
        logger.debug("根据key从Redis中获取对象 key [" + key + "]");
        try {
            if (key == null) {
                return null;
            }else{
                byte[] rawValue = (byte[]) redisTemplate.opsForValue().get(getStringKey(key));
                return  (V) SerializableUtil.deserialize(rawValue);
            }
        } catch (Throwable t) {
            throw new CacheException(t);
        }

    }

    @Override
    public V put(K key, V value) throws CacheException {
        logger.debug("根据key从存储 key [" + key + "]");
        try {
            redisTemplate.opsForValue().set(getStringKey(key), SerializableUtil.serialize(value));
            redisTemplate.expire(getStringKey(key),expire, TimeUnit.SECONDS);
            return value;
        } catch (Throwable t) {
            throw new CacheException(t);
        }
    }

    @Override
    public V remove(K key) throws CacheException {
        logger.debug("从redis中删除 key [" + key + "]");
        try {
            V previous = get(key);
            redisTemplate.delete(getStringKey(key));
            return previous;
        } catch (Throwable t) {
            throw new CacheException(t);
        }
    }

    @Override
    public void clear() throws CacheException {
        logger.debug("从redis中删除所有shiro元素");
        try {
            redisTemplate.delete(keys());
        } catch (Throwable t) {
            throw new CacheException(t);
        }
    }

    @Override
    public int size() {
        try {
            return keys().size();
        } catch (Throwable t) {
            throw new CacheException(t);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public Set<K> keys() {
        try {
            Set<byte[]> keys =redisTemplate.keys(this.shiroKeys + "*");
            if (CollectionUtils.isEmpty(keys)) {
                return Collections.emptySet();
            }else{
                Set<K> newKeys = new HashSet<K>();
                for(byte[] key:keys){
                    newKeys.add((K)key);
                }
                return newKeys;
            }
        } catch (Throwable t) {
            throw new CacheException(t);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public Collection<V> values() {
        try {
            Set<byte[]> keys = redisTemplate.keys(this.shiroKeys + "*");
            if (!CollectionUtils.isEmpty(keys)) {
                List<V> values = new ArrayList<V>(keys.size());
                for (byte[] key : keys) {
                    @SuppressWarnings("unchecked")
                    V value = get((K)key);
                    if (value != null) {
                        values.add(value);
                    }
                }
                return Collections.unmodifiableList(values);
            } else {
                return Collections.emptyList();
            }
        } catch (Throwable t) {
            throw new CacheException(t);
        }
    }
}

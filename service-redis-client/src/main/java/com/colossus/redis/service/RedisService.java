package com.colossus.redis.service;

import com.colossus.redis.service.histrix.RedisServiceHystrix;
import org.springframework.cache.CacheManager;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Set;

@FeignClient(value = "service-redis",fallback = RedisServiceHystrix.class)
public interface RedisService {

    @GetMapping(value = "get")
    String get(@RequestParam("key") String key);

    @PostMapping(value = "set")
    String set(@RequestParam("key") String key,@RequestParam("value") String value);

    @GetMapping(value = "hget")
    String hget(@RequestParam("hkey") String hkey,@RequestParam("key") String key);

    @PostMapping(value = "hset")
    void hset(@RequestParam("hkey") String hkey, @RequestParam("key") String key,@RequestParam("value") String value);

    @PostMapping(value = "incr")
    long incr(@RequestParam("key") String key,long amout);

    @PostMapping(value = "expire")
    void expire(@RequestParam("key") String key,@RequestParam("second")  Integer second);

    @GetMapping(value = "ttl")
    long ttl(@RequestParam("key") String key);

    @PostMapping(value = "del")
    void del(@RequestParam("key") String key);

    @PostMapping(value = "hdel")
    long hdel(@RequestParam("hkey") String hkey,@RequestParam("key")  String key);

    @GetMapping(value = "get-cacheManager")
    CacheManager getCacheManager();

    @GetMapping(value = "get-keys")
    Set<String> keys(@RequestParam("hkey") String hkey);

    @GetMapping(value = "hlen")
    long hlen(@RequestParam("hkey") String hkey);
}


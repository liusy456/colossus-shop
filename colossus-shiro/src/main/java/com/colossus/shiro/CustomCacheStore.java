package com.colossus.shiro;


import org.apache.shiro.cache.Cache;
import org.pac4j.core.store.AbstractStore;

public class CustomCacheStore<K, O> extends AbstractStore<K, O> {
    private Cache<K, O> cache;

    public CustomCacheStore() {}

    public CustomCacheStore(Cache<K, O> cache) {
       this.cache=cache;
    }

    @Override
    protected O internalGet(final K key) {
        return cache.get(key);
    }

    @Override
    protected void internalSet(final K key, final O value) {
        cache.put(key, value);
    }

    @Override
    protected void internalRemove(final K key) {
        cache.remove(key);
    }

    public Cache<K, O> getCache() {
        return cache;
    }

}

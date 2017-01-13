package com.daedafusion.cache.providers;

import com.daedafusion.cache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;
import net.sf.ehcache.config.CacheConfiguration;
import net.sf.ehcache.config.PersistenceConfiguration;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.UUID;

/**
 * Created by mphilpot on 7/9/14.
 */
public class MemoryOnlyEhcache<K, V> implements Cache<K, V>
{
    private static final Logger log = Logger.getLogger(MemoryOnlyEhcache.class);

    private Ehcache cache;

    public static class Builder
    {
        private String name;
        private String size;
        private Long ttl;

        public Builder(String name)
        {
            this.name = name;
        }

        public Builder size(String size)
        {
            this.size = size;
            return this;
        }

        public Builder ttl(Long ttl)
        {
            this.ttl = ttl;
            return this;
        }

        public MemoryOnlyEhcache build(){
            CacheConfiguration config = new CacheConfiguration();
            config.setMaxBytesLocalHeap(this.size != null ? this.size : "20M");
            if(this.ttl != null)
            {
                config.setTimeToLiveSeconds(this.ttl);
            }
            config.persistence(new PersistenceConfiguration().strategy(PersistenceConfiguration.Strategy.NONE));
            config.setMemoryStoreEvictionPolicy("LRU");

            config.setName(String.format("%s-%s", this.name, UUID.randomUUID().toString()));

            net.sf.ehcache.Cache cache = new net.sf.ehcache.Cache(config);
            CacheManager.getInstance().addCache(cache);

            return new MemoryOnlyEhcache(cache);
        }
    }

    MemoryOnlyEhcache(Ehcache cache)
    {
        this.cache = cache;
    }

    @Override
    public void put(K key, V value)
    {
        cache.put(new Element(key, value));
    }

    @Override
    public void put(K key, V value, int ttl)
    {
        Element e = new Element(key, value);
        e.setTimeToLive(ttl);
        cache.put(e);
    }

    @Override
    public V get(K key)
    {
        Element e = cache.get(key);

        if(e == null)
        {
            return null;
        }

        return (V) e.getObjectValue();
    }

    @Override
    public boolean contains(K key)
    {
        return cache.get(key) != null;
    }

    @Override
    public void close() throws IOException
    {
        if(cache != null)
        {
            cache.dispose();
        }
    }
}

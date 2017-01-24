package com.daedafusion.cache.providers;

import com.daedafusion.cache.Cache;
import org.apache.log4j.Logger;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.io.IOException;

/**
 * Created by mphilpot on 1/23/17.
 */
public class RedisCache implements Cache<String, String>
{
    private static final Logger log = Logger.getLogger(RedisCache.class);
    private final JedisPool pool;
    private final String cacheName;

    public RedisCache(JedisPool pool, String cacheName)
    {
        this.pool = pool;
        this.cacheName = cacheName;
    }


    @Override
    public void put(String key, String value)
    {
        try(Jedis jedis = pool.getResource())
        {
            jedis.set(String.format("%s:%s", cacheName, key), value);
        }
    }

    @Override
    public void put(String key, String value, int ttl)
    {
        try(Jedis jedis = pool.getResource())
        {
            jedis.set(String.format("%s:%s", cacheName, key), value);
            jedis.expire(String.format("%s:%s", cacheName, key), ttl);
        }
    }

    @Override
    public String get(String key)
    {
        try(Jedis jedis = pool.getResource())
        {
            return jedis.get(String.format("%s:%s", cacheName, key));
        }
    }

    @Override
    public boolean contains(String key)
    {
        try(Jedis jedis = pool.getResource())
        {
            return jedis.get(String.format("%s:%s", cacheName, key)) != null;
        }
    }

    @Override
    public void close() throws IOException
    {
        // Closing of pool handled by manager
    }
}

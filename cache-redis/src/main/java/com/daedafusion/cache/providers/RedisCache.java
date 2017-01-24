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

    public RedisCache(JedisPool pool)
    {
        this.pool = pool;
    }


    @Override
    public void put(String key, String value)
    {
        try(Jedis jedis = pool.getResource())
        {
            jedis.set(key, value);
        }
    }

    @Override
    public void put(String key, String value, int ttl)
    {
        try(Jedis jedis = pool.getResource())
        {
            jedis.set(key, value);
            jedis.expire(key, ttl);
        }
    }

    @Override
    public String get(String key)
    {
        try(Jedis jedis = pool.getResource())
        {
            return jedis.get(key);
        }
    }

    @Override
    public boolean contains(String key)
    {
        try(Jedis jedis = pool.getResource())
        {
            return jedis.get(key) != null;
        }
    }

    @Override
    public void close() throws IOException
    {
        // Closing of pool handled by manager
    }
}

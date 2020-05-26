package com.daedafusion.cache.providers;

import com.daedafusion.cache.Cache;
import org.apache.log4j.Logger;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.ScanParams;
import redis.clients.jedis.ScanResult;

import java.io.IOException;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.util.Set;

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

    private String getPrefixedKey(String key)
    {
        return String.format("%s:%s", cacheName, key);
    }

    @Override
    public void put(String key, String value)
    {
        try(Jedis jedis = pool.getResource())
        {
            jedis.set(getPrefixedKey(key), value);
        }
    }

    @Override
    public void put(String key, String value, int ttl)
    {
        try(Jedis jedis = pool.getResource())
        {
            jedis.set(getPrefixedKey(key), value);
            jedis.expire(getPrefixedKey(key), ttl);
        }
    }

    @Override
    public String get(String key)
    {
        try(Jedis jedis = pool.getResource())
        {
            return jedis.get(getPrefixedKey(key));
        }
    }

    @Override
    public boolean contains(String key)
    {
        try(Jedis jedis = pool.getResource())
        {
            return jedis.get(getPrefixedKey(key)) != null;
        }
    }

    @Override
    public void remove(String key)
    {
        try(Jedis jedis = pool.getResource())
        {
            jedis.del(getPrefixedKey(key));
        }
    }

    @Override
    public void removeAsync(String key) {
        try(Jedis jedis = pool.getResource())
        {
            jedis.unlink(getPrefixedKey(key));
        }
    }

    @Override
    public void setAddItems(String key, String... values) {
        try(Jedis jedis = pool.getResource())
        {
            jedis.sadd(getPrefixedKey(key), values);
        }
    }

    @Override
    public Set<String> setGetMembers(String key) {
        try(Jedis jedis = pool.getResource())
        {
            return jedis.smembers(getPrefixedKey(key));
        }
    }

    @Override
    public void close() throws IOException
    {
        // Closing of pool handled by manager
    }

    @Override
    public void removePattern(String key)
    {
        try(Jedis jedis = pool.getResource())
        {
            ScanParams scanParams = new ScanParams().count(10).match(getPrefixedKey(key));
            String cur = redis.clients.jedis.ScanParams.SCAN_POINTER_START;
            ScanResult<String> scanResult = jedis.scan(cur, scanParams);

            while(!scanResult.isCompleteIteration() || scanResult.getResult().size() > 0) {
                jedis.unlink(scanResult.getResult().toArray(new String[0]));
                scanResult = jedis.scan(scanResult.getCursor());
            }
        }
    }
}

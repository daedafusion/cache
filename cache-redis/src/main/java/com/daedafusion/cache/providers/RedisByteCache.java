package com.daedafusion.cache.providers;

import com.daedafusion.cache.Cache;
import org.apache.log4j.Logger;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.ScanParams;
import redis.clients.jedis.ScanResult;

import java.io.IOException;
import java.util.Set;

/**
 * Created by mphilpot on 1/23/17.
 */
public class RedisByteCache implements Cache<byte[], byte[]>
{
    private static final Logger log = Logger.getLogger(RedisByteCache.class);
    private final JedisPool pool;
    private final String cacheName;

    public RedisByteCache(JedisPool pool, String cacheName)
    {
        this.pool = pool;
        this.cacheName = cacheName;
    }

    @Override
    public void put(byte[] key, byte[] value)
    {
        try(Jedis jedis = pool.getResource())
        {
            jedis.set(key, value);
        }
    }

    @Override
    public void put(byte[] key, byte[] value, int ttl)
    {
        try(Jedis jedis = pool.getResource())
        {
            jedis.set(key, value);
            jedis.expire(key, ttl);
        }
    }

    @Override
    public byte[] get(byte[] key)
    {
        try(Jedis jedis = pool.getResource())
        {
            return jedis.get(key);
        }
    }

    @Override
    public boolean contains(byte[] key)
    {
        try(Jedis jedis = pool.getResource())
        {
            return jedis.get(key) != null;
        }
    }

    @Override
    public void remove(byte[] key)
    {
        try(Jedis jedis = pool.getResource())
        {
            jedis.del(key);
        }
    }

    @Override
    public void removeAsync(byte[] key) {
        try(Jedis jedis = pool.getResource())
        {
            jedis.unlink(key);
        }
    }

    @Override
    public void setAddItems(byte[] key, byte[]... values) {
        try(Jedis jedis = pool.getResource())
        {
            jedis.sadd(key, values);
        }
    }

    @Override
    public Set<byte[]> setGetMembers(byte[] key) {
        try(Jedis jedis = pool.getResource())
        {
            return jedis.smembers(key);
        }
    }

    @Override
    public void close() throws IOException
    {
        // Closing of pool handled by manager
    }


    @Override
    public void removePattern(byte[] key)
    {
        try(Jedis jedis = pool.getResource())
        {
            ScanParams scanParams = new ScanParams().count(10).match(key);
            String cur = redis.clients.jedis.ScanParams.SCAN_POINTER_START;
            ScanResult<byte[]> scanResult = jedis.scan(cur.getBytes(), scanParams);

            // Scan can be complete with results AND scan be ongoing with no results yet
            while(!scanResult.isCompleteIteration() || scanResult.getResult().size() > 0) {
                if(scanResult.getResult().size() > 0) {
                    jedis.unlink(scanResult.getResult().toArray(new byte[0][0]));
                }
                scanResult = jedis.scan(scanResult.getCursor().getBytes());
            }
        }
    }
}

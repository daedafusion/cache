package com.daedafusion.cache.providers;

import com.daedafusion.cache.Cache;
import com.daedafusion.configuration.Configuration;
import com.daedafusion.sf.AbstractProvider;
import com.daedafusion.sf.LifecycleListener;
import org.apache.log4j.Logger;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by mphilpot on 1/23/17.
 */
public class RedisCacheManager extends AbstractProvider implements CacheManagerProvider
{
    private static final Logger log = Logger.getLogger(RedisCacheManager.class);

    private final Map<String, Cache> map;
    private JedisPool pool;

    public RedisCacheManager()
    {
        map = new HashMap<>();

        addLifecycleListener(new LifecycleListener()
        {
            @Override
            public void init()
            {

            }

            @Override
            public void start()
            {
                pool = new JedisPool(new JedisPoolConfig(), Configuration.getInstance().getString("redis.hostname", "localhost"));
            }

            @Override
            public void stop()
            {

            }

            @Override
            public void teardown()
            {
                pool.destroy();
            }
        });
    }

    @Override
    public Cache getCache(String name)
    {
        if(getProperty(String.format("%s.bytes", name)) != null)
        {
            return new RedisByteCache(pool, name);
        }
        else
        {
            return new RedisCache(pool, name);
        }
    }

    @Override
    public boolean hasCache(String name)
    {
        return map.containsKey(name) || getProperty(name) != null;
    }

    @Override
    public boolean isDefaultProvider()
    {
        return Boolean.parseBoolean(getProperty("defaultProvider", "false"));
    }
}

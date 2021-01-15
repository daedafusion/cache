package com.daedafusion.cache.providers;

import com.daedafusion.cache.Cache;
import com.daedafusion.configuration.Configuration;
import com.daedafusion.sf.AbstractProvider;
import com.daedafusion.sf.LifecycleListener;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by mphilpot on 8/27/14.
 */
public class MemoryOnlyEhcacheManager extends AbstractProvider implements CacheManagerProvider
{
    private static final Logger log = LogManager.getLogger(MemoryOnlyEhcacheManager.class);

    private final Map<String, Cache> map;

    public MemoryOnlyEhcacheManager()
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

            }

            @Override
            public void stop()
            {

            }

            @Override
            public void teardown()
            {
                map.values().forEach(cache -> {
                    try
                    {
                        cache.close();
                    }
                    catch (IOException e)
                    {
                        log.warn("", e);
                    }
                });

                map.clear();
            }
        });
    }

    @Override
    public Cache getCache(String name)
    {
        synchronized (map)
        {
            if(!map.containsKey(name))
            {
                MemoryOnlyEhcache.Builder builder = new MemoryOnlyEhcache.Builder(name);
                if(Configuration.getInstance().getString(String.format("cache.%s.size", name), getProperty(String.format("%s.size", name))) != null)
                {
                    builder.size(getProperty(String.format("%s.size", name)));
                }
                if(Configuration.getInstance().getString(String.format("cache.%s.ttl", name), getProperty(String.format("%s.ttl", name))) != null)
                {
                    builder.ttl(Long.parseLong(getProperty(String.format("%s.ttl", name))));
                }
                map.put(name, builder.build());
            }

            return map.get(name);
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
        return Boolean.parseBoolean(getProperty("defaultProvider", "true"));
    }
}

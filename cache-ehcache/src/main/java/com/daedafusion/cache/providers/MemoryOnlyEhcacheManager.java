package com.daedafusion.cache.providers;

import com.daedafusion.cache.Cache;
import com.daedafusion.sf.AbstractProvider;
import com.daedafusion.sf.LifecycleListener;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by mphilpot on 8/27/14.
 */
public class MemoryOnlyEhcacheManager extends AbstractProvider implements CacheManagerProvider
{
    private static final Logger log = Logger.getLogger(MemoryOnlyEhcacheManager.class);

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
                if(getProperty(String.format("%s.size", name)) != null)
                {
                    builder.size(getProperty(String.format("%s.size", name)));
                }
                if(getProperty(String.format("%s.ttl", name)) != null)
                {
                    builder.ttl(Long.parseLong(getProperty(String.format("%s.ttl", name))));
                }
                map.put(name, builder.build());
            }

            return map.get(name);
        }
    }
}

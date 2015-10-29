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
                for(Cache c : map.values())
                {
                    try
                    {
                        c.close();
                    }
                    catch (IOException e)
                    {
                        log.warn("", e);
                    }
                }

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
                map.put(name, new MemoryOnlyEhcache(getProperty(name, "20M"), name));
            }

            return map.get(name);
        }
    }
}

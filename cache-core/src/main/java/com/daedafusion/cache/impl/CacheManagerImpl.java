package com.daedafusion.cache.impl;

import com.daedafusion.cache.Cache;
import com.daedafusion.cache.CacheManager;
import com.daedafusion.cache.providers.CacheManagerProvider;
import com.daedafusion.sf.AbstractService;
import com.daedafusion.sf.LifecycleListener;
import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by mphilpot on 8/27/14.
 */
public class CacheManagerImpl extends AbstractService<CacheManagerProvider> implements CacheManager
{
    private static final Logger log = Logger.getLogger(CacheManagerImpl.class);

    private CacheManagerProvider defaultProvider;
    private final Map<String, CacheManagerProvider> cacheMap;

    public CacheManagerImpl()
    {
        cacheMap = new HashMap<>();

        addLifecycleListener(new LifecycleListener()
        {
            @Override
            public void init()
            {

            }

            @Override
            public void start()
            {
                getProviders().stream().filter(CacheManagerProvider::isDefaultProvider).forEach(provider -> {
                    if(defaultProvider == null)
                    {
                        defaultProvider = provider;
                    }
                    else
                    {
                        log.warn(String.format("Multiple cache providers configured as default. Using %s", defaultProvider.getClass().getName()));
                    }
                });

                if(defaultProvider == null)
                {
                    log.warn("No default cache provider configured. This could result in undesirable consequences (NullPointerExceptions)");
                }
            }

            @Override
            public void stop()
            {

            }

            @Override
            public void teardown()
            {

            }
        });
    }

    @Override
    public Cache getCache(String name)
    {
        if(!cacheMap.containsKey(name))
        {
            getProviders().forEach(provider ->
            {
                if (provider.hasCache(name))
                {
                    cacheMap.put(name, provider);
                }
            });
        }

        return cacheMap.getOrDefault(name, defaultProvider).getCache(name);
    }

    @Override
    public Class getProviderInterface()
    {
        return CacheManagerProvider.class;
    }
}

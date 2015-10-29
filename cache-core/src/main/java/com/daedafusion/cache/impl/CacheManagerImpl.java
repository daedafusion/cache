package com.daedafusion.cache.impl;

import com.daedafusion.cache.Cache;
import com.daedafusion.cache.CacheManager;
import com.daedafusion.cache.providers.CacheManagerProvider;
import com.daedafusion.sf.AbstractService;
import org.apache.log4j.Logger;

/**
 * Created by mphilpot on 8/27/14.
 */
public class CacheManagerImpl extends AbstractService<CacheManagerProvider> implements CacheManager
{
    private static final Logger log = Logger.getLogger(CacheManagerImpl.class);

    @Override
    public Cache getCache(String name)
    {
        return getSingleProvider().getCache(name);
    }

    @Override
    public Class getProviderInterface()
    {
        return CacheManagerProvider.class;
    }
}

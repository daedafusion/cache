package com.daedafusion.cache.providers;

import com.daedafusion.cache.CacheManager;
import com.daedafusion.sf.Provider;

/**
 * Created by mphilpot on 8/27/14.
 */
public interface CacheManagerProvider extends Provider, CacheManager
{
    /**
     * @param name name of cache (key)
     * @return true if this provider has been configured with a cache by the given name
     */
    boolean hasCache(String name);

    /**
     * @return true if this provider should be relied upon for on-demand cache
     */
    boolean isDefaultProvider();
}

package com.daedafusion.cache;

/**
 * Created by mphilpot on 8/27/14.
 */
public interface CacheManager
{
    /**
     * Return the named cache.  If the name does not already exist, the manager should create a default one
     * @param name
     * @return a Cache object
     */
    Cache getCache(String name);
}

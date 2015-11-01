package com.daedafusion.cache;

import java.io.Closeable;

/**
 * Created by mphilpot on 8/27/14.
 */
public interface Cache<K, V> extends Closeable
{
    /**
     * Store key/value pair in cache
     * @param key
     * @param value
     */
    void put(K key, V value);

    /**
     * Retrieve key/value pair
     * @param key
     * @return value associated with key
     */
    V get(K key);

    /**
     * Check if key exists
     * @param key
     * @return true if key is in cache
     */
    boolean contains(K key);
}

package com.daedafusion.cache.providers;

import org.apache.log4j.Logger;
import org.junit.Ignore;
import org.junit.Test;
import redis.clients.jedis.JedisPool;

import java.util.stream.IntStream;

@Ignore
public class RedisIT {
    private static final Logger log = Logger.getLogger(RedisIT.class);

    @Test
    public void testScanString() {
        JedisPool pool = new JedisPool();

        RedisCache redis = new RedisCache(pool, "scanString");

        IntStream.range(0, 20).forEach(i -> redis.put(String.format("a:b:c:%d", i), String.valueOf(i)));

        log.info(redis.get("a:b:c:11"));

        redis.removePattern("a:b:c:*");

        log.info(redis.get("a:b:c:11"));
    }

    @Test
    public void testScanBytes() throws InterruptedException {
        JedisPool pool = new JedisPool();

        RedisByteCache redis = new RedisByteCache(pool, "scanString");

        IntStream.range(0, 25).forEach(i -> redis.put(String.format("a:b:c:%d", i).getBytes(), String.valueOf(i).getBytes()));

        log.info(redis.get("a:b:c:22".getBytes()));

        redis.removePattern("a:b:c:*".getBytes());

        Thread.sleep(3000);

        log.info(redis.get("a:b:c:22".getBytes()));
    }
}

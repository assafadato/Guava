package com.guava.example.cache;

import com.guava.example.cache.CachedService;
import com.guava.example.cache.config.ApplicationContext;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheStats;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = ApplicationContext.class)
public class CacheTest {

    private CacheStats cacheStats;

    @Autowired private Cache cache;
    @Autowired private CachedService cachedService;

    @Before
    public void setUp() throws Exception {
        cacheStats = cache.stats();
        cache.invalidateAll();
        assertEquals("cache is not empty", 0, cache.size());
    }

    @Test
    public void testCacheableWithParameters() throws Exception {
        for (int i = 0; i < 10; i++) {
            cachedService.heavyLiftingMethod("one");
            cachedService.heavyLiftingMethod("two");
            cachedService.heavyLiftingMethod("three");
        }
        cacheStats = cache.stats().minus(cacheStats);
        assertEquals("Incorrect hit count", 27, cacheStats.hitCount());
        assertEquals("Incorrect miss count", 3, cacheStats.missCount());
        assertEquals("Incorrect cache size", 3, cache.size());
    }

    @Test
    public void testCacheableWithoutParameters() throws Exception {
        for (int i = 0; i < 10; i++) {
            cachedService.heavyLiftingMethod();
        }
        cacheStats = cache.stats().minus(cacheStats);
        assertEquals("Incorrect hit count", 9, cacheStats.hitCount());
        assertEquals("Incorrect miss count", 1, cacheStats.missCount());
        assertEquals("Incorrect cache size", 1, cache.size());
    }

    @Test
    public void testCacheEntryExpired() throws Exception {
        cachedService.heavyLiftingMethod("one");
        cachedService.heavyLiftingMethod("one");
        cachedService.heavyLiftingMethod("one");
        cacheStats = cache.stats().minus(cacheStats);
        assertEquals("Incorrect hit count", 2, cacheStats.hitCount());
        assertEquals("Incorrect miss count", 1, cacheStats.missCount());
        assertEquals("Incorrect eviction count", 0, cacheStats.evictionCount());
        assertEquals("Incorrect cache size", 1, cache.size());
        TimeUnit.SECONDS.sleep(5);
        cachedService.heavyLiftingMethod("one");
        cacheStats = cache.stats().minus(cacheStats);
        assertEquals("Incorrect hit count", 0, cacheStats.hitCount());
        assertEquals("Incorrect miss count", 1, cacheStats.missCount());
        assertEquals("Incorrect eviction count", 1, cacheStats.evictionCount());
        assertEquals("Incorrect cache size", 1, cache.size());

    }
}
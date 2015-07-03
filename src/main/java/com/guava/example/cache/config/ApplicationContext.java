package com.guava.example.cache.config;

import com.guava.example.cache.CachedServiceImpl;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.collect.Lists;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.guava.GuavaCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

/**
 * Created by Assaf on 7/2/2015.
 */
@Configuration
@ComponentScan(basePackageClasses = {CachedServiceImpl.class, CachedServiceImpl.class})
@EnableCaching
public class ApplicationContext {

    public static final String CACHE_NAME = "test-cache";

    @Bean
    public Cache cache() {
        return CacheBuilder.newBuilder().recordStats().maximumSize(10).expireAfterWrite(2, TimeUnit.SECONDS).build();
    }

    @Bean
    public CacheManager cacheManager() {
        SimpleCacheManager cacheManager = new SimpleCacheManager();
        GuavaCache cache = new GuavaCache(CACHE_NAME, cache());
        cacheManager.setCaches(Lists.newArrayList(cache));
        return cacheManager;
    }
}

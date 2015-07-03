package com.guava.example.cache;

import com.guava.example.cache.config.ApplicationContext;
import org.springframework.cache.annotation.Cacheable;

/**
 * Created by Assaf on 7/3/2015.
 */
public interface CachedService {

    @Cacheable(value = ApplicationContext.CACHE_NAME)
    String heavyLiftingMethod();

    @Cacheable(value = ApplicationContext.CACHE_NAME)
    String heavyLiftingMethod(String something);
}

package com.guava.example.cache;

import org.springframework.stereotype.Component;

@Component
public class CachedServiceImpl implements CachedService {

    @Override
    public String heavyLiftingMethod() {
        return "Saying hello";
    }

    @Override
    public String heavyLiftingMethod(String something) {
        return "saying " + something;
    }
}

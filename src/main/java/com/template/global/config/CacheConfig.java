package com.template.global.config;


import com.github.benmanes.caffeine.cache.Caffeine;
import com.template.global.common.enums.CacheType;
import com.template.global.util.CustomKeyGenerator;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Configuration
@EnableCaching
public class CacheConfig {
    @Bean
    public CacheManager cacheManager() {
        SimpleCacheManager cacheManager = new SimpleCacheManager();

        List<CaffeineCache> caches = Arrays.stream(CacheType.values())
                .map(cache ->
                        new CaffeineCache(
                                cache.getName(),
                                Caffeine.newBuilder()
                                        .expireAfterWrite(cache.getExpireAfterWrite(), TimeUnit.SECONDS)
                                        .maximumSize(cache.getMaximumSize())
                                        .recordStats()
                                        .build()
                        ))
                .collect(Collectors.toList());

        cacheManager.setCaches(caches);
        return cacheManager;
    }

    @Bean("customKeyGenerator")
    public KeyGenerator keyGenerator() {
        return new CustomKeyGenerator();
    }
}

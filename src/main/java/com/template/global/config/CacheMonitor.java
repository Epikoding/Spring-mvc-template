package com.template.global.config;

import com.github.benmanes.caffeine.cache.stats.CacheStats;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Component
@RequiredArgsConstructor
public class CacheMonitor {
    private final CacheManager cacheManager;

    public void printCacheInfo() {
        Collection<String> cacheNames = cacheManager.getCacheNames();
        for (String cacheName : cacheNames) {
            Cache cache = cacheManager.getCache(cacheName);
            if (cache != null) {
                Object nativeCache = cache.getNativeCache();
                if (nativeCache instanceof com.github.benmanes.caffeine.cache.Cache) {
                    com.github.benmanes.caffeine.cache.Cache<Object, Object> caffeineCache =
                            (com.github.benmanes.caffeine.cache.Cache<Object, Object>) nativeCache;
                    long size = caffeineCache.estimatedSize();
                    CacheStats stats = caffeineCache.stats();
                    System.out.println("Cache Name: " + cacheName);
                    System.out.println("Size: " + size);
                    System.out.println("Hit Count: " + stats.hitCount());
                    System.out.println("Miss Count: " + stats.missCount());
                    System.out.println("Eviction Count: " + stats.evictionCount());

                    // 캐시 키 정보 출력
                    System.out.println("Cache Keys:");
                    caffeineCache.asMap().forEach((key, value) -> System.out.println("- " + key));

                    System.out.println("---------------------------");
                }
            }
        }
    }
}
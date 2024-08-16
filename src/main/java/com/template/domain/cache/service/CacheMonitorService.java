package com.template.domain.cache.service;

import com.github.benmanes.caffeine.cache.stats.CacheStats;
import com.template.domain.cache.dto.CacheInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class CacheMonitorService {
    private final CacheManager cacheManager;

    public List<CacheInfo> getCacheInfo() {
        List<CacheInfo> cacheInfoList = new ArrayList<>();
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
                    List<Object> cacheKeys = new ArrayList<>(caffeineCache.asMap().keySet());

                    CacheInfo cacheInfo = new CacheInfo(cacheName, size, stats.hitCount(),
                            stats.missCount(), stats.evictionCount(), cacheKeys);
                    cacheInfoList.add(cacheInfo);
                }
            }
        }
        return cacheInfoList;
    }

    public Collection<String> clearAllCaches() {
        Collection<String> cacheNames = cacheManager.getCacheNames();
        cacheNames.stream()
                .map(cacheManager::getCache)
                .filter(Objects::nonNull)
                .forEach(Cache::clear);

        return cacheNames;
    }
}
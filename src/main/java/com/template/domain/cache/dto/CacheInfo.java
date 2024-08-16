package com.template.domain.cache.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class CacheInfo {
    private String cacheName;
    private long size;
    private long hitCount;
    private long missCount;
    private long evictionCount;
    private List<Object> cacheKeys;
}
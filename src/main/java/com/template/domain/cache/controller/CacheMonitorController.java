package com.template.domain.cache.controller;

import com.template.domain.cache.dto.CacheInfo;
import com.template.domain.cache.service.CacheMonitorService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/cache")
public class CacheMonitorController {
    private final CacheMonitorService cacheMonitorService;

    @GetMapping
    public List<CacheInfo> getCacheInfo() {
        return cacheMonitorService.getCacheInfo();
    }

    @GetMapping("/clear")
    public Collection<String> clearAlLCaches() {
        return cacheMonitorService.clearAllCaches();
    }
}
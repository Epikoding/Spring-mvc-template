package com.template.global.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@Configuration
@RequiredArgsConstructor
public class CacheMonitorScheduler {
    private final CacheMonitor cacheMonitor;

//    @Scheduled(fixedDelay = 10 * 1000) // 10 seconds in milliseconds
    public void scheduleCacheMonitoring() {
        cacheMonitor.printCacheInfo();
    }
}
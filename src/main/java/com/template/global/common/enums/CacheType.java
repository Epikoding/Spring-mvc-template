package com.template.global.common.enums;

import lombok.Getter;

@Getter
public enum CacheType { // 3600 = 1시간, 86400 = 1일, 604800 = 1주일,

    // CacheNameConfig
    USER_CACHE(CacheNameConfig.USER_CACHE, 86400, 10000),
    USER_LIST_CACHE(CacheNameConfig.USER_LIST_CACHE, 86400, 5),

    // CacheTimeConfig
    CACHE_5_SEC(CacheTimeConfig.CACHE_5_SECOND, 5, 10000),
    CACHE_10_SEC(CacheTimeConfig.CACHE_10_SECOND, 10, 10000),
    CACHE_20_SEC(CacheTimeConfig.CACHE_20_SECOND, 20, 10000),
    CACHE_30_SEC(CacheTimeConfig.CACHE_30_SECOND, 30, 10000),
    CACHE_60_SEC(CacheTimeConfig.CACHE_60_SECOND, 60, 10000),
    CACHE_120_SEC(CacheTimeConfig.CACHE_120_SECOND, 120, 10000),
    CACHE_300_SEC(CacheTimeConfig.CACHE_300_SECOND, 300, 10000)

    ;

    private final String name;
    private final int expireAfterWrite;
    private final int maximumSize;

    CacheType(String name, int expireAfterWrite, int maximumSize) {
        this.name = name;
        this.expireAfterWrite = expireAfterWrite;
        this.maximumSize = maximumSize;
    }

    public static class CacheTimeConfig {
        public static final String CACHE_5_SECOND = "cache5Sec";
        public static final String CACHE_10_SECOND = "cache10Sec";
        public static final String CACHE_20_SECOND = "cache20Sec";
        public static final String CACHE_30_SECOND = "cache30Sec";
        public static final String CACHE_60_SECOND = "cache60Sec";
        public static final String CACHE_120_SECOND = "cache120Sec";
        public static final String CACHE_300_SECOND = "cache300Sec";
    }

    public static class CacheNameConfig {
        public static final String USER_CACHE = "userCache";
        public static final String USER_LIST_CACHE = "userListCache";
    }
}



















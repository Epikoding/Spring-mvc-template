package com.template.global.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;

public class CustomKeyGenerator implements KeyGenerator {
    private static final Logger logger = LoggerFactory.getLogger(CustomKeyGenerator.class);

    @Override
    public Object generate(Object target, Method method, Object... params) {
        String key = target.getClass().getSimpleName() + "_"
                + method.getName() + "_"
                + StringUtils.arrayToDelimitedString(params, "_");

        logger.info("Generated cache key: {}", key);

        return key;
    }
}
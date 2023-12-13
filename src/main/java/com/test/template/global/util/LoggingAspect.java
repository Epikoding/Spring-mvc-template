package com.test.template.global.util;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.WebRequest;

import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class LoggingAspect {
    @Before(value = "@annotation(org.springframework.web.bind.annotation.ExceptionHandler)")
    public void logBefore(JoinPoint joinPoint) {
        WebRequest request = (WebRequest) Arrays.stream(joinPoint.getArgs())
                .filter(arg -> arg instanceof WebRequest)
                .findFirst()
                .orElse(null);
        if (Objects.nonNull(request)) {
            logRequestDetails(request);
        }
    }

    private void logRequestDetails(WebRequest request) {
        String requestURI = request.getDescription(false).replace("uri=", ""); // Gets the request URI
        String queryParams = request.getParameterMap().entrySet().stream()
                .map(entry -> entry.getKey() + "=" + Arrays.toString(entry.getValue()))
                .collect(Collectors.joining("&"));

        log.error("Exception occurred at URI: {} with params: {}", requestURI, queryParams);
    }
}

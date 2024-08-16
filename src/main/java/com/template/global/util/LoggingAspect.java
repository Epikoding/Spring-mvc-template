package com.template.global.util;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.WebRequest;

import java.util.Arrays;
import java.util.Collections;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class LoggingAspect {

    private static final String BASE_PACKAGE = "com.test";
    private final Set<Integer> loggedExceptions = Collections.newSetFromMap(new ConcurrentHashMap<>());


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



    @AfterThrowing(pointcut = "within(" + BASE_PACKAGE + "..*)", throwing = "exception")
    public void logException(JoinPoint joinPoint, Exception exception) {
        if (loggedExceptions.add(System.identityHashCode(exception))) {
            logExceptionDetails(joinPoint, exception);
        }
    }

    private void logExceptionDetails(JoinPoint joinPoint, Exception exception) {
        String exceptionName = exception.getClass().getSimpleName();
        String className = joinPoint.getTarget().getClass().getSimpleName();
        String methodName = joinPoint.getSignature().getName();

        // Log the high-level exception details
        log.error("{} occurred in class: {}, method: {}", exceptionName, className, methodName);

        // Extract and log relevant stack trace elements
        Stream.of(exception.getStackTrace())
                .filter(element -> element.getClassName().startsWith(BASE_PACKAGE))
                .forEach(element -> log.error("    at {}({}:{})", element.getClassName(), element.getFileName(), element.getLineNumber()));

        // Optionally, log request details if available
        WebRequest request = getRequestFromArgs(joinPoint.getArgs());
        if (request != null) {
            logRequestDetails(request);
        }
    }

    private WebRequest getRequestFromArgs(Object[] args) {
        return (WebRequest) Arrays.stream(args)
                .filter(arg -> arg instanceof WebRequest)
                .findFirst()
                .orElse(null);
    }
}

package com.template.global.util;


import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class RequestLoggingFilter implements Filter {

    private static final Logger logger = LoggerFactory.getLogger(RequestLoggingFilter.class);

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        MultiReadHttpServletRequest wrappedRequest = new MultiReadHttpServletRequest((HttpServletRequest) request);
        String content = getBody(wrappedRequest);
        String requestType = wrappedRequest.getMethod();
        String servletPath = wrappedRequest.getServletPath();
        String clientIP = request.getRemoteAddr();
        Map<String, String[]> parameterMap = wrappedRequest.getParameterMap();

        String parameters = "";
        if (parameterMap != null && !parameterMap.isEmpty()) {
            parameters = parameterMap.entrySet().stream()
                    .map(entry -> entry.getKey() + "=" + Arrays.toString(entry.getValue()))
                    .collect(Collectors.joining(", "));
        }

        if (!Objects.equals(servletPath, "/actuator/prometheus")) {
            if (Objects.equals(requestType, "GET")) {
                logger.info("Request type: {}, from IP: {}, with body: {}, to servletPath: {}{}",
                        requestType, clientIP, content, servletPath,
                        parameters.isEmpty() ? "" : ", parameters: " + parameters);

            } else if (Objects.equals(requestType, "POST")) {
                logger.info("Request type: {}, from IP: {}, to servletPath: {}, with body: {}",
                        requestType, clientIP, servletPath, content);
            }
        }

        chain.doFilter(wrappedRequest, response);
    }

    private String getBody(HttpServletRequest request) throws IOException {
        StringBuilder stringBuilder = new StringBuilder(1024);
        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(request.getInputStream()))) {
            char[] charBuffer = new char[128];
            int bytesRead;
            while ((bytesRead = bufferedReader.read(charBuffer)) != -1) {
                stringBuilder.append(charBuffer, 0, bytesRead);
            }
        }
        return stringBuilder.toString();
    }
}


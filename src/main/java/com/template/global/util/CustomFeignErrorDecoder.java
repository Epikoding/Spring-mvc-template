package com.template.global.util;

import com.template.global.exception.CustomFeignClientException;
import feign.Response;
import feign.Request;
import feign.codec.ErrorDecoder;

import java.nio.charset.StandardCharsets;
import java.util.Objects;

public class CustomFeignErrorDecoder implements ErrorDecoder {
    private final ErrorDecoder defaultDecoder = new Default();

    @Override
    public Exception decode(String methodKey, Response response) {
        if (Objects.equals(response.status(), 404)) {
            String arguments = extractArguments(response.request());

            return new CustomFeignClientException("Resource not found in Feign client request: " + arguments, response.status());
        }
        return defaultDecoder.decode(methodKey, response); // Default handling for other errors
    }

    private String extractArguments(Request request) {
        if (Objects.nonNull(request.body()) && request.body().length > 0) {
            try {
                return new String(request.body(), Objects.nonNull(request.charset()) ? request.charset() : StandardCharsets.UTF_8);
            } catch (Exception e) {
//                log.error("Error parsing request body", e);
            }
        }
        return "No arguments";
    }

}


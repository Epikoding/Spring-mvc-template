package com.template.global.exception;

import lombok.Getter;

@Getter
public class CustomFeignClientException extends RuntimeException {
    private final int status;

    public CustomFeignClientException(String message, int status) {
        super(message, null, false, false); // Disable stack trace
        this.status = status;
    }
}


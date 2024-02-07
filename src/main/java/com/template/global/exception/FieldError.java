package com.template.global.exception;

import lombok.Getter;

@Getter
public class FieldError {
    private String field;
    private String reason;

    public FieldError(String field, String reason) {
        this.field = field;
        this.reason = reason;
    }
}

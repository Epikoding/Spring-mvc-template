package com.template.global.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Setter
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Result {
    private int status;
    private Object message;
    private Object data;

    private Result(HttpStatus httpStatus, Object message, Object data) {
        this.status = httpStatus.value();
        this.message = message;
        this.data = data;
    }

    public static ResponseEntity<Result> of(HttpStatus status) {
        return ResponseEntity.status(status)
                .body(new Result(status, status.getReasonPhrase(), null));
    }

    public static ResponseEntity<Result> of(HttpStatus status, Object data) {
        return ResponseEntity.status(status)
                .body(new Result(status, status.getReasonPhrase(), data));
    }

    public static ResponseEntity<Result> of(HttpStatus status, String message) {
        return ResponseEntity.status(status)
                .body(new Result(status, message, null));
    }

    public static ResponseEntity<Result> of(HttpStatus status, String message, Object data) {
        return ResponseEntity.status(status)
                .body(new Result(status, message, data));
    }

    public static ResponseEntity<Result> ok(Object data) {
        return of(HttpStatus.OK, data);
    }

    public static ResponseEntity<Result> created(Object data) {
        return of(HttpStatus.CREATED, data);
    }

    public static ResponseEntity<Result> badRequest(String message) {
        return of(HttpStatus.BAD_REQUEST, message);
    }

    public static ResponseEntity<Result> notFound(String message) {
        return of(HttpStatus.NOT_FOUND, message);
    }
}
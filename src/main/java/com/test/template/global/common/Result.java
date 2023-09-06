package com.test.template.global.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.test.template.global.exception.FieldError;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.util.List;

@Setter
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Result {
    private int status;
    private Object message;
    private Object data;

    public Result() {
        this.status = HttpStatus.OK.value();
        this.message = HttpStatus.OK.getReasonPhrase();
    }

    public Result(Object data) {
        this.status = HttpStatus.OK.value();
        this.message = HttpStatus.OK.getReasonPhrase();
        this.data = data;
    }

    public Result(HttpStatus httpStatus){
        this.status = httpStatus.value();
        this.message = httpStatus.getReasonPhrase();
    }


    public Result(HttpStatus httpStatus, FieldError fieldError) {
        this.status = httpStatus.value();
        this.message = fieldError;
    }

    public Result(HttpStatus httpStatus, List<FieldError> fieldErrorList) {
        this.status = httpStatus.value();
        this.message = fieldErrorList;
    }

    public Result(HttpStatus httpStatus, String message) {
        this.status = httpStatus.value();
        this.message = message;
    }
}
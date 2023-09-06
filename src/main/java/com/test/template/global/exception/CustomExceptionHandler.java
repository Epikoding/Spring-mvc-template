package com.test.template.global.exception;

import com.test.template.global.common.Result;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@ControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Result> entityNotFoundException(EntityNotFoundException ex) {
        return new ResponseEntity<>(new Result(HttpStatus.NO_CONTENT), HttpStatusCode.valueOf(HttpStatus.NO_CONTENT.value()));
    }

    @ExceptionHandler(EntityExistsException.class)
    public ResponseEntity<Result> entityExistsException(EntityExistsException ex) {
        return new ResponseEntity<>(new Result(HttpStatus.CONFLICT), HttpStatusCode.valueOf(HttpStatus.CONFLICT.value()));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Result> IllegalArgumentException(IllegalArgumentException exception) {
        if (Objects.isNull(exception.getMessage())) {
            return new ResponseEntity<>(new Result(HttpStatus.NO_CONTENT), HttpStatusCode.valueOf(HttpStatus.NO_CONTENT.value()));
        } else {
            Result errorResult = new Result(HttpStatus.NO_CONTENT, exception.getMessage());
            return ResponseEntity.badRequest().body(errorResult);
        }
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<Result> handleNoSuchElementException(NoSuchElementException exception) {
        if (Objects.isNull(exception.getMessage())) {
            return new ResponseEntity<>(new Result(HttpStatus.NOT_FOUND), HttpStatusCode.valueOf(HttpStatus.NOT_FOUND.value()));
        } else {
            Result errorResult = new Result(HttpStatus.NOT_FOUND, exception.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResult);
        }
    }


    /**
     * @Valid or @Validated 으로 binding error 발생시 발생한다.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Result> handleValidationExceptions(MethodArgumentNotValidException exception) {
        List<org.springframework.validation.FieldError> exceptionFieldErrors = exception.getFieldErrors();
        List<FieldError> fieldErrorList = exceptionFieldErrors.stream()
                .map(fieldError -> new FieldError(fieldError.getField(), fieldError.getDefaultMessage()))
                .collect(Collectors.toList());
        Result errorResult = new Result(HttpStatus.BAD_REQUEST, fieldErrorList);
        return ResponseEntity.badRequest().body(errorResult);
    }

    /**
     * 메서드 인수의 타입과 입력된 값의 타입이 맞지않는 경우 발생
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    protected ResponseEntity<Result> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException exception) {
        FieldError fieldError = new FieldError(exception.getName(), exception.getMessage());
        Result errorResult = new Result(HttpStatus.BAD_REQUEST, fieldError);
        return ResponseEntity.badRequest().body(errorResult);
    }


    /**
     * 필수값이 없는 경우 발생
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    protected ResponseEntity<Result> handleMissingServletRequestParameterException(MissingServletRequestParameterException exception) {
        FieldError fieldError = new FieldError(exception.getParameterName(), exception.getMessage());
        Result errorResult = new Result(HttpStatus.BAD_REQUEST, fieldError);
        return ResponseEntity.badRequest().body(errorResult);
    }

    /**
     * Authentication 객체가 필요한 권한을 보유하지 않은 경우 발생합니다.
     */
    @ExceptionHandler(AccessDeniedException.class)
    protected ResponseEntity<Result> handleAccessDeniedException(AccessDeniedException e) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new Result(HttpStatus.FORBIDDEN));
    }

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<Result> handleException(Exception ex) {
        log.error("Internal Server Error", ex);
        return ResponseEntity.internalServerError().body(new Result(HttpStatus.INTERNAL_SERVER_ERROR));
    }
}

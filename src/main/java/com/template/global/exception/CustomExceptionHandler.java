package com.template.global.exception;

import com.template.global.common.Result;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.connector.ClientAbortException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.context.request.async.AsyncRequestTimeoutException;
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
    public ResponseEntity<Result> entityNotFoundException(EntityNotFoundException exception) {
        return Result.notFound(exception.getMessage());
    }

    @ExceptionHandler(EntityExistsException.class)
    public ResponseEntity<Result> entityExistsException(EntityExistsException exception) {
        return Result.of(HttpStatus.CONFLICT, exception.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Result> illegalArgumentException(IllegalArgumentException exception) {
        return Objects.isNull(exception.getMessage())
                ? Result.of(HttpStatus.BAD_REQUEST)
                : Result.badRequest(exception.getMessage());
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<Result> handleNoSuchElementException(NoSuchElementException exception) {
        return Objects.isNull(exception.getMessage())
                ? Result.of(HttpStatus.NOT_FOUND)
                : Result.notFound(exception.getMessage());
    }

    @ExceptionHandler(HttpClientErrorException.class)
    public ResponseEntity<Result> handleHttpClientErrorException(HttpClientErrorException exception) {
        log.error("HttpClientErrorException: {}", exception.getMessage());
        return Result.of(HttpStatus.valueOf(exception.getStatusCode().value()), exception.getStatusText());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Result> handleValidationExceptions(MethodArgumentNotValidException exception) {
        List<FieldError> fieldErrors = exception.getFieldErrors().stream()
                .map(fieldError -> new FieldError(fieldError.getField(), fieldError.getDefaultMessage()))
                .collect(Collectors.toList());
        return Result.of(HttpStatus.BAD_REQUEST, fieldErrors);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    protected ResponseEntity<Result> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException exception) {
        FieldError fieldError = new FieldError(exception.getName(), exception.getMessage());
        return Result.of(HttpStatus.BAD_REQUEST, fieldError);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    protected ResponseEntity<Result> handleMissingServletRequestParameterException(MissingServletRequestParameterException exception) {
        FieldError fieldError = new FieldError(exception.getParameterName(), exception.getMessage());
        return Result.of(HttpStatus.BAD_REQUEST, fieldError);
    }

    @ExceptionHandler(AccessDeniedException.class)
    protected ResponseEntity<Result> handleAccessDeniedException(AccessDeniedException exception) {
        return Result.of(HttpStatus.FORBIDDEN, exception);
    }

    @ExceptionHandler(AsyncRequestTimeoutException.class)
    public ResponseEntity<Result> handleAsyncRequestTimeoutException(AsyncRequestTimeoutException exception) {
        return Result.of(HttpStatus.REQUEST_TIMEOUT, exception);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Result> handleJSONParseError(HttpMessageNotReadableException exception) {
        Throwable mostSpecificCause = exception.getMostSpecificCause();
        String errorMessage = mostSpecificCause.getMessage();
        log.error("JSON parse error: {}", errorMessage);
        return Result.badRequest(errorMessage);
    }

    @ExceptionHandler(CustomFeignClientException.class)
    public ResponseEntity<Result> handleFeignClientException(CustomFeignClientException exception) {
        log.error("FeignClientException: {}", exception.getMessage());
        return Result.of(HttpStatus.valueOf(exception.getStatus()), exception.getMessage());
    }

    @ExceptionHandler(ClientAbortException.class)
    public void handleClientAbortException(ClientAbortException exception) {
        log.debug("ClientAbortException: Broken pipe - client likely disconnected. Message: {}", exception.getMessage());
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<Result> handleIllegalStateException(IllegalStateException exception) {
        log.error("IllegalStateException: {}", exception.getMessage());
        return Result.badRequest(exception.getMessage());
    }

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<Result> handleException(Exception exception) {
        if (Objects.nonNull(exception.getCause())) {
            log.error("Exception occurred: {}; Cause: {}", exception.getMessage(), exception.getCause().toString());
        } else {
            log.error("Exception occurred: {}", exception.getMessage());
        }
        return Result.of(HttpStatus.INTERNAL_SERVER_ERROR);
    }
}


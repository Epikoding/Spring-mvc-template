package com.template.global.exception;

import com.template.global.common.Result;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.context.request.async.AsyncRequestTimeoutException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.nio.file.AccessDeniedException;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CustomExceptionHandlerTest {

    @InjectMocks
    private CustomExceptionHandler exceptionHandler;

    @Mock
    private WebRequest webRequest;


    @Test
    void testEntityNotFoundException() {
        EntityNotFoundException exception = new EntityNotFoundException("Entity not found");
        ResponseEntity<Result> response = exceptionHandler.entityNotFoundException(exception);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Entity not found", Objects.requireNonNull(response.getBody()).getMessage());
    }

    @Test
    void testIllegalArgumentException() {
        IllegalArgumentException exception = new IllegalArgumentException("Invalid argument");
        ResponseEntity<Result> response = exceptionHandler.illegalArgumentException(exception);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Invalid argument", Objects.requireNonNull(response.getBody()).getMessage());
    }

    @Test
    void testMethodArgumentTypeMismatchException() {
        MethodArgumentTypeMismatchException exception = mock(MethodArgumentTypeMismatchException.class);
        when(exception.getName()).thenReturn("paramName");
        when(exception.getMessage()).thenReturn("Type mismatch");

        ResponseEntity<Result> response = exceptionHandler.handleMethodArgumentTypeMismatchException(exception);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void testAccessDeniedException() {
        AccessDeniedException exception = new AccessDeniedException("Access denied");
        ResponseEntity<Result> response = exceptionHandler.handleAccessDeniedException(exception);

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }

    @Test
    void testAsyncRequestTimeoutException() {
        AsyncRequestTimeoutException exception = new AsyncRequestTimeoutException();
        ResponseEntity<Result> response = exceptionHandler.handleAsyncRequestTimeoutException(exception);

        AsyncRequestTimeoutException data = (AsyncRequestTimeoutException) Objects.requireNonNull(response.getBody()).getData();
        String message = data.getBody().getTitle();

        assertEquals(HttpStatus.REQUEST_TIMEOUT, response.getStatusCode());
        assertEquals("Service Unavailable", message);
    }

    @Test
    void testHttpMessageNotReadableException() {
        HttpMessageNotReadableException exception = mock(HttpMessageNotReadableException.class);
        Throwable cause = new RuntimeException("JSON parse error");
        when(exception.getMostSpecificCause()).thenReturn(cause);

        ResponseEntity<Result> response = exceptionHandler.handleJSONParseError(exception);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("JSON parse error", Objects.requireNonNull(response.getBody()).getMessage());
    }

    @Test
    void testCustomFeignClientException() {
        CustomFeignClientException exception = new CustomFeignClientException("Feign client error", HttpStatus.BAD_GATEWAY.value());
        ResponseEntity<Result> response = exceptionHandler.handleFeignClientException(exception);

        assertEquals(HttpStatus.BAD_GATEWAY, response.getStatusCode());
        assertEquals("Feign client error", Objects.requireNonNull(response.getBody()).getMessage());
    }

    @Test
    void testIllegalStateException() {
        IllegalStateException exception = new IllegalStateException("Illegal state");
        ResponseEntity<Result> response = exceptionHandler.handleIllegalStateException(exception);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Illegal state", Objects.requireNonNull(response.getBody()).getMessage());
    }

    @Test
    void testGenericException() {
        Exception exception = new Exception("Generic error");
        ResponseEntity<Result> response = exceptionHandler.handleException(exception);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }
}
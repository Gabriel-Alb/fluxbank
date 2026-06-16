package com.gabriel.fluxbank.exception;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ApiError> handleBusinessException(
            BusinessException exception,
            HttpServletRequest request
    ) {
        HttpStatus status = exception.getStatus();

        ApiError error = ApiError.of(
                status.value(),
                status.getReasonPhrase(),
                exception.getMessage(),
                request.getRequestURI()
        );

        return ResponseEntity
                .status(status)
                .body(error);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleValidationException(
            MethodArgumentNotValidException exception,
            HttpServletRequest request
    ) {
        List<ApiError.FieldError> fieldErrors =
                exception.getBindingResult()
                        .getFieldErrors()
                        .stream()
                        .map(error -> new ApiError.FieldError(
                                error.getField(),
                                error.getDefaultMessage()
                        ))
                        .toList();

        HttpStatus status = HttpStatus.BAD_REQUEST;

        ApiError error = ApiError.of(
                status.value(),
                status.getReasonPhrase(),
                "Validation failed",
                request.getRequestURI(),
                fieldErrors
        );

        return ResponseEntity
                .status(status)
                .body(error);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiError> handleUnreadableMessage(
            HttpMessageNotReadableException exception,
            HttpServletRequest request
    ) {
        HttpStatus status = HttpStatus.BAD_REQUEST;

        ApiError error = ApiError.of(
                status.value(),
                status.getReasonPhrase(),
                "Malformed JSON request",
                request.getRequestURI()
        );

        return ResponseEntity
                .status(status)
                .body(error);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleGenericException(
            Exception exception,
            HttpServletRequest request
    ) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;

        ApiError error = ApiError.of(
                status.value(),
                status.getReasonPhrase(),
                "An unexpected error occurred",
                request.getRequestURI()
        );

        return ResponseEntity
                .status(status)
                .body(error);
    }
}
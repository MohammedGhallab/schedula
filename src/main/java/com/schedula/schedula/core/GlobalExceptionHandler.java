package com.schedula.schedula.core;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;
import org.springframework.dao.DataIntegrityViolationException;

import com.schedula.schedula.exceptions.TooManyException;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

        @ExceptionHandler(MethodArgumentNotValidException.class)
        public DynamicResponseEntity handleValidation(MethodArgumentNotValidException ex) {
                Map<String, String> errors = new HashMap<>();
                ex.getBindingResult().getFieldErrors().forEach(f -> errors.put(f.getField(), f.getDefaultMessage()));

                log.warn("Validation failed: {}", errors);
                return new DynamicResponseEntity(HttpStatus.BAD_REQUEST, null, errors);
        }


        @ExceptionHandler(EntityNotFoundException.class)
        public DynamicResponseEntity handleNotFound(EntityNotFoundException ex) {
                Map<String, String> error = Map.of("error", ex.getMessage());
                return new DynamicResponseEntity(HttpStatus.NOT_FOUND, null, error);
        }


        @ExceptionHandler(RuntimeException.class)
        public DynamicResponseEntity handleRuntimeException(RuntimeException ex) {
                log.error("Runtime error: ", ex);
                String message = (ex.getMessage() != null) ? ex.getMessage() : "Unexpected runtime error";
                Map<String, String> error = Map.of("message", message);

                return new DynamicResponseEntity(HttpStatus.BAD_REQUEST, null, error);
        }

        @ExceptionHandler(TooManyException.class)
        public DynamicResponseEntity handleTooManyException(TooManyException ex) {
                Map<String, String> error = Map.of("message", ex.getMessage());
                return new DynamicResponseEntity(HttpStatus.TOO_MANY_REQUESTS, null, error);
        }

        @ExceptionHandler(DataIntegrityViolationException.class)
        public DynamicResponseEntity handleDataIntegrity(DataIntegrityViolationException ex) {
                log.error("Database conflict: {}", ex.getMostSpecificCause().getMessage());
                Map<String, String> error = Map.of("message",
                                "Data integrity violation. This record might already exist.");

                return new DynamicResponseEntity(HttpStatus.CONFLICT, null, error);
        }

        @ExceptionHandler(TransactionSystemException.class)
        public DynamicResponseEntity handleTransactionException(TransactionSystemException ex) {
                Throwable cause = ex.getRootCause();
                if (cause instanceof ConstraintViolationException consEx) {
                        Map<String, String> errors = new HashMap<>();
                        consEx.getConstraintViolations()
                                        .forEach(v -> errors.put(v.getPropertyPath().toString(), v.getMessage()));
                        return new DynamicResponseEntity(HttpStatus.BAD_REQUEST, null, errors);
                }

                return new DynamicResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR, null,
                                Map.of("error", "Transaction failed"));
        }

        @ExceptionHandler(NoResourceFoundException.class)
        public DynamicResponseEntity handleNoResourceFound(NoResourceFoundException ex) {
                Map<String, String> error = Map.of("message", "Endpoint not found: " + ex.getResourcePath());
                return new DynamicResponseEntity(HttpStatus.NOT_FOUND, null, error);
        }

        @ExceptionHandler(Exception.class)
        public DynamicResponseEntity handleGeneralException(Exception ex) {
                log.error("Critical System Error: ", ex);
                Map<String, Object> error = new HashMap<>();
                error.put("message", "An internal server error occurred");
                error.put("type", ex.getClass().getSimpleName());

                return new DynamicResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR, null, error);
        }
}
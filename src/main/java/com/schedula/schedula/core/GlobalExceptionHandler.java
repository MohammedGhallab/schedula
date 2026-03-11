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
        // 1. أخطاء التحقق من البيانات (Validation @Valid) -> 400
        @ExceptionHandler(MethodArgumentNotValidException.class)
        public DynamicResponseEntity handleValidation(MethodArgumentNotValidException ex) {
                Map<String, String> errors = new HashMap<>();
                ex.getBindingResult().getFieldErrors().forEach(f -> errors.put(f.getField(), f.getDefaultMessage()));

                log.warn("Validation failed: {}", errors);
                return new DynamicResponseEntity(HttpStatus.BAD_REQUEST, null, errors);
        }

        // 2. معالجة "العنصر غير موجود" (Custom Exception) -> 404
        @ExceptionHandler(EntityNotFoundException.class)
        public DynamicResponseEntity handleNotFound(EntityNotFoundException ex) {
                Map<String, String> error = Map.of("error", ex.getMessage());
                return new DynamicResponseEntity(HttpStatus.NOT_FOUND, null, error);
        }

        // 3. أخطاء الـ Runtime (مع معالجة الـ Null Message) -> 400
        @ExceptionHandler(RuntimeException.class)
        public DynamicResponseEntity handleRuntimeException(RuntimeException ex) {
                log.error("Runtime error: ", ex);
                String message = (ex.getMessage() != null) ? ex.getMessage() : "Unexpected runtime error";
                Map<String, String> error = Map.of("message", message);

                return new DynamicResponseEntity(HttpStatus.BAD_REQUEST, null, error);
        }

        // 4. أخطاء الـ Rate Limiting (Too Many Requests) -> 429
        @ExceptionHandler(TooManyException.class)
        public DynamicResponseEntity handleTooManyException(TooManyException ex) {
                Map<String, String> error = Map.of("message", ex.getMessage());
                return new DynamicResponseEntity(HttpStatus.TOO_MANY_REQUESTS, null, error);
        }

        // 5. أخطاء قاعدة البيانات (Data Integrity) -> 409
        @ExceptionHandler(DataIntegrityViolationException.class)
        public DynamicResponseEntity handleDataIntegrity(DataIntegrityViolationException ex) {
                log.error("Database conflict: {}", ex.getMostSpecificCause().getMessage());
                Map<String, String> error = Map.of("message",
                                "Data integrity violation. This record might already exist.");

                return new DynamicResponseEntity(HttpStatus.CONFLICT, null, error);
        }

        // 6. أخطاء الـ Transaction و Bean Validation العميقة -> 400
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

        // 7. معالجة خطأ "المسار غير موجود" (404 Resource) -> 404
        @ExceptionHandler(NoResourceFoundException.class)
        public DynamicResponseEntity handleNoResourceFound(NoResourceFoundException ex) {
                Map<String, String> error = Map.of("message", "Endpoint not found: " + ex.getResourcePath());
                return new DynamicResponseEntity(HttpStatus.NOT_FOUND, null, error);
        }

        // 8. المعالج العام النهائي (Catch-all) -> 500
        @ExceptionHandler(Exception.class)
        public DynamicResponseEntity handleGeneralException(Exception ex) {
                log.error("Critical System Error: ", ex);
                Map<String, Object> error = new HashMap<>();
                error.put("message", "An internal server error occurred");
                error.put("type", ex.getClass().getSimpleName());

                return new DynamicResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR, null, error);
        }
}
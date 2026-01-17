package com.schedula.schedula.core;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.schedula.schedula.exceptions.TooManyException;

import lombok.extern.slf4j.Slf4j;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
        @ExceptionHandler(MethodArgumentNotValidException.class)
        public DynamicResponseEntity handleValidationExceptions(MethodArgumentNotValidException ex) {
                Map<String, String> errors = new HashMap<>();
                ex.getBindingResult().getFieldErrors()
                                .forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
                return new DynamicResponseEntity(HttpStatus.BAD_REQUEST, null, errors);
        }

        @ExceptionHandler(RuntimeException.class)
        public DynamicResponseEntity handleRuntimeException(RuntimeException ex) {
                Map<String, String> errors = new HashMap<>();
                errors.put("message", ex.getMessage());
                System.out.println("RuntimeException : " + ex.getMessage());
                return new DynamicResponseEntity(HttpStatus.BAD_REQUEST, null, errors);
        }

        @ExceptionHandler(TooManyException.class)
        public DynamicResponseEntity handleMethodNotFoundException(TooManyException ex) {
                Map<String, String> errors = new HashMap<>();
                errors.put("message", ex.getMessage());
                System.out.println("TooManyException : " + ex.getMessage());
                return new DynamicResponseEntity(HttpStatus.TOO_MANY_REQUESTS, null, errors);
        }

        // 3. معالجة أي خطأ عام آخر (لضمان عدم انهيار التطبيق)
        @ExceptionHandler(Exception.class)
        public DynamicResponseEntity handleGeneralException(Exception ex) {
                Map<String, String> errors = new HashMap<>();
                errors.put("message", ex.getMessage() + "  :   " + ex.getLocalizedMessage());
                // بمجرد مناداة هذه الدالة، سيتم إرسال إيميل فوراً لأن المستوى ERROR
                log.error("error type : ", ex.getClass().getName());
                return new DynamicResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR, null, errors);
        }
}
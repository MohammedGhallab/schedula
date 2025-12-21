package com.schedula.schedula.core;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.el.MethodNotFoundException;
import lombok.extern.slf4j.Slf4j;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseCustom> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors()
                .forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
        return new ResponseEntity<>(new ErrorResponseCustom(HttpStatus.BAD_REQUEST.value(), "MethodArgumentNotValidException Error",
                System.currentTimeMillis(), errors), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodNotFoundException.class)
    public ResponseEntity<ErrorResponseCustom> handleNotFound(MethodNotFoundException ex) {
        Map<String, String> errors = new HashMap<>();
        errors.put("message", ex.getMessage());
        return new ResponseEntity<>(new ErrorResponseCustom(HttpStatus.NOT_FOUND.value(), "MethodNotFoundException Error",
                System.currentTimeMillis(), errors), HttpStatus.NOT_FOUND);
    }

    // 3. معالجة أي خطأ عام آخر (لضمان عدم انهيار التطبيق)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseCustom> handleGeneralException(Exception ex) {
        Map<String, String> errors = new HashMap<>();
        errors.put("message", ex.getMessage());
        // بمجرد مناداة هذه الدالة، سيتم إرسال إيميل فوراً لأن المستوى ERROR
        log.error("error : ", ex.getMessage());
        return new ResponseEntity<>(new ErrorResponseCustom(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Exception Error", System.currentTimeMillis(), errors), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
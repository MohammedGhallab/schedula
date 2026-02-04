package com.schedula.schedula.core;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.schedula.schedula.exceptions.TooManyException;

import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
        @ExceptionHandler(MethodArgumentNotValidException.class)
        public DynamicResponseEntity handleValidationExceptions(MethodArgumentNotValidException ex) {
                Map<String, String> errors = new HashMap<>();
                ex.getBindingResult().getFieldErrors()
                                .forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
                System.out.println("MethodArgumentNotValidException : " + ex.getMessage());
                return new DynamicResponseEntity(HttpStatus.BAD_REQUEST, null, errors);
        }

        @ExceptionHandler(RuntimeException.class)
        public DynamicResponseEntity handleRuntimeException(RuntimeException ex) {
                Map<String, String> errors = new HashMap<>();
                errors.put("message", ex.getMessage().replace(" ", ""));
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

        // 2. معالجة أخطاء الـ Transaction (مثل الخطأ الذي ظهر لك)
        @ExceptionHandler(TransactionSystemException.class)
        public ResponseEntity<Map<String, String>> handleTransactionException(TransactionSystemException ex) {
                Throwable cause = ex.getRootCause();

                // إذا كان السبب هو مخالفة شروط قاعدة البيانات (Bean Validation)
                if (cause instanceof ConstraintViolationException) {
                        ConstraintViolationException consEx = (ConstraintViolationException) cause;
                        Map<String, String> errors = new HashMap<>();
                        consEx.getConstraintViolations().forEach(violation -> {
                                String fieldName = violation.getPropertyPath().toString();
                                String message = violation.getMessage();
                                errors.put(fieldName, message);
                        });
                        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
                }

                // خطأ غير معروف في المعاملة
                Map<String, String> error = new HashMap<>();
                error.put("error", "حدث خطأ أثناء حفظ البيانات في قاعدة البيانات");
                return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
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
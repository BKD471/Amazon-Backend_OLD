package com.phoenix.amazon.AmazonBackend.exceptions;

import com.phoenix.amazon.AmazonBackend.dto.ErrorDetails;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler({UserExceptions.class,UserNotFoundExceptions.class, BadApiRequestExceptions.class})
    public ResponseEntity<ErrorDetails> handleAllUncheckedCustomException(Exception e, WebRequest web) {
        ErrorDetails error = new ErrorDetails.builder()
                .timeStamp(LocalTime.now())
                .message(e.getMessage())
                .details(web.getDescription(false)).build();

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<Map<String, String>> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().stream().forEachOrdered((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);

        });
        return new ResponseEntity<>(errors,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDetails> handleGenericException(Exception e, WebRequest web) {
        ErrorDetails error = new ErrorDetails.builder()
                .timeStamp(LocalTime.now())
                .message(e.getMessage())
                .details(web.getDescription(false)).build();
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

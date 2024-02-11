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
import java.util.UUID;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler({UserExceptions.class, UserNotFoundExceptions.class, BadApiRequestExceptions.class})
    public ResponseEntity<ErrorDetails> handleAllUncheckedCustomException(Exception e, WebRequest web) {
        ErrorDetails error = new ErrorDetails.builder()
                .timeStamp(LocalTime.now())
                .message(e.getMessage())
                .details(web.getDescription(false)).build();

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<ErrorDetails> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, WebRequest web) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName;
            try {
                fieldName = ((FieldError) error).getField();
            } catch (ClassCastException e) {
                fieldName = UUID.randomUUID().toString();
            }
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);

        });
        ErrorDetails errorDetails = new ErrorDetails.builder()
                .details(ex.getMessage())
                .message(ex.getMessage())
                .timeStamp(LocalTime.now())
                .error(errors)
                .details(web.getDescription(false))
                .build();
        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
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

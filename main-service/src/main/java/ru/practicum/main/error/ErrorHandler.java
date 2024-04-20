package ru.practicum.main.error;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.postgresql.util.PSQLException;
import ru.practicum.main.error.exception.AccessException;
import ru.practicum.main.error.exception.BadRequestException;
import ru.practicum.main.error.exception.ConflictException;
import ru.practicum.main.error.exception.NotFoundException;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<ApiError> handleConflictException(ConflictException ex) {
        ApiError response = ApiError.builder()
                .status(HttpStatus.CONFLICT.name())
                .reason("Request is incorrect.")
                .message(ex.getMessage())
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ApiError> handleBadRequestException(BadRequestException ex) {
        ApiError response = ApiError.builder()
                .status(HttpStatus.BAD_REQUEST.name())
                .reason("Request is incorrect.")
                .message(ex.getMessage())
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ApiError> handleNotFoundException(NotFoundException ex) {
        ApiError response = ApiError.builder()
                .status(HttpStatus.NOT_FOUND.name())
                .reason("Request is incorrect.")
                .message(ex.getMessage())
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler({PSQLException.class})
    public ResponseEntity<ApiError> handlePSQLException(final PSQLException psqlException) {
        ApiError apiError = ApiError.builder()
                .status(String.valueOf(HttpStatus.CONFLICT))
                .reason("Duplicate value in database")
                .message(psqlException.getMessage())
                .timestamp(LocalDateTime.now())
                .build();
        return new ResponseEntity<>(apiError, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(AccessException.class)
    public ResponseEntity<ApiError> handleAccessException(final AccessException e) {
        ApiError response = ApiError.builder()
                .status(String.valueOf(HttpStatus.FORBIDDEN))
                .reason("The action is not available")
                .message(e.getMessage())
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, Object> errors = new HashMap<>();
        BindingResult bindingResult = ex.getBindingResult();
        for (FieldError fieldError : bindingResult.getFieldErrors()) {
            errors.put(fieldError.getField(), fieldError.getDefaultMessage());
        }

        ApiError response = ApiError.builder()
                .status(HttpStatus.BAD_REQUEST.name())
                .reason("Validation failed")
                .message("Validation error(s) occurred")
                .errors(errors)
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }
}
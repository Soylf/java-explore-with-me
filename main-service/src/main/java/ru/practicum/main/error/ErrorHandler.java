package ru.practicum.main.error;

import org.springframework.http.HttpStatus;
import org.postgresql.util.PSQLException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.main.error.exception.AccessException;
import ru.practicum.main.error.exception.BadRequestException;
import ru.practicum.main.error.exception.ConflictException;
import ru.practicum.main.error.exception.NotFoundException;

import java.time.LocalDateTime;
import java.util.Objects;

@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<ApiError> handleConflictException(ConflictException ex) {
        return buildErrorResponse(HttpStatus.CONFLICT, "Request is incorrect.", ex.getMessage());
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ApiError> handleBadRequestException(BadRequestException ex) {
        return buildErrorResponse(HttpStatus.BAD_REQUEST, "Request is incorrect.", ex.getMessage());
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ApiError> handleNotFoundException(NotFoundException ex) {
        return buildErrorResponse(HttpStatus.NOT_FOUND, "Resource not found.", ex.getMessage());
    }

    @ExceptionHandler(PSQLException.class)
    public ResponseEntity<ApiError> handlePSQLException(PSQLException ex) {
        return buildErrorResponse(HttpStatus.CONFLICT, "Duplicate value in database.", ex.getMessage());
    }

    @ExceptionHandler(AccessException.class)
    public ResponseEntity<ApiError> handleAccessException(AccessException ex) {
        return buildErrorResponse(HttpStatus.FORBIDDEN, "Access forbidden.", ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleValidationException(MethodArgumentNotValidException ex) {
        String message = Objects.requireNonNull(ex.getBindingResult().getFieldError()).getDefaultMessage();
        return buildErrorResponse(HttpStatus.BAD_REQUEST, "Validation failed.", message);
    }

    @ExceptionHandler(Throwable.class)
    public ResponseEntity<ApiError> handleThrowable(Throwable ex) {
        return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error.", ex.getMessage());
    }

    private ResponseEntity<ApiError> buildErrorResponse(HttpStatus status, String reason, String message) {
        ApiError response = ApiError.builder()
                .status(status.name())
                .reason(reason)
                .message(message)
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.status(status).body(response);
    }
}
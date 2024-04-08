package ru.practicum.evm.error;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.evm.error.exception.BadRequestException;
import ru.practicum.evm.error.exception.ConflictException;
import ru.practicum.evm.error.exception.NotFoundException;

import java.time.LocalDateTime;
import java.util.HashMap;

@Slf4j
@RestControllerAdvice
public class ErrorHandler {
    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<ApiError> handleConflictException(ConflictException ex) {
        return buildResponseEntity(HttpStatus.CONFLICT, ex.getMessage(), "Conflict occurred", ex);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ApiError> handleBadRequestException(BadRequestException ex) {
        return buildResponseEntity(HttpStatus.BAD_REQUEST, ex.getMessage(), "Bad Request", ex);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ApiError> handleNotFoundException(NotFoundException ex) {
        return buildResponseEntity(HttpStatus.NOT_FOUND, ex.getMessage(), "Not Found", ex);
    }

    private ResponseEntity<ApiError> buildResponseEntity(HttpStatus status, String message, String reason, Exception ex) {
        ApiError apiError = new ApiError(message, new HashMap<>(), reason, status.toString(), LocalDateTime.now());
        log.error("Exception occurred: {}", ex.getMessage(), ex);
        return new ResponseEntity<>(apiError, status);
    }
}
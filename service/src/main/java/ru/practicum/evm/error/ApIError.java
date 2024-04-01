package ru.practicum.evm.error;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@AllArgsConstructor
public class ApIError {
    private String message;
    private Map<String, Object> errors;
    private String reason;
    private String status;
    private LocalDateTime timestamp;
}
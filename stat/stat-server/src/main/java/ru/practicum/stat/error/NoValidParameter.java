package ru.practicum.stat.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class NoValidParameter extends RuntimeException {
    public NoValidParameter(String message) {
        super(message);
    }
}

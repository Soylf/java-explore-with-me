package ru.practicum.statsserver.error.exception;

public class NoValidParameter extends RuntimeException {
    public NoValidParameter(String message) {
        super(message);
    }
}

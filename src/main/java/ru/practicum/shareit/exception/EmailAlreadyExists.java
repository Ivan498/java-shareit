package ru.practicum.shareit.exception;

public class EmailAlreadyExists extends RuntimeException {
    public EmailAlreadyExists() {
        super();
    }

    public EmailAlreadyExists(String message) {
        super(message);
    }

    public EmailAlreadyExists(String message, Throwable cause) {
        super(message, cause);
    }

    public EmailAlreadyExists(Throwable cause) {
        super(cause);
    }
}


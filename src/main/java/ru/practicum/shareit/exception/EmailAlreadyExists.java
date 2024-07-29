package ru.practicum.shareit.exception;

public class EmailAlreadyExists extends RuntimeException {
    public EmailAlreadyExists() {
        super();
    }

    public EmailAlreadyExists(String message) {
        super(message);
    }
}


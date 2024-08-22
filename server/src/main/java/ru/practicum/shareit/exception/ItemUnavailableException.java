package ru.practicum.shareit.exception;

public class ItemUnavailableException extends RuntimeException {
    public ItemUnavailableException(String string) {
        super(string);
    }
}

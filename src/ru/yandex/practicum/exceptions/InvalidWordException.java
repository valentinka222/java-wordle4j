package ru.yandex.practicum.exceptions;

public class InvalidWordException extends WordleGameException {
    public InvalidWordException(String message) {
        super(message);
    }
}

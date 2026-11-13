package ru.yandex.practicum.exceptions;

public class DictionaryLoadException extends WordleSystemException {
    public DictionaryLoadException(String message, Throwable cause) {
        super(message, cause);
    }
}

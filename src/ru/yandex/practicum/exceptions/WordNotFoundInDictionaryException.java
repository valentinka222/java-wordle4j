package ru.yandex.practicum.exceptions;

public class WordNotFoundInDictionaryException extends RuntimeException {
  public WordNotFoundInDictionaryException(String message) {
    super(message);
  }
}

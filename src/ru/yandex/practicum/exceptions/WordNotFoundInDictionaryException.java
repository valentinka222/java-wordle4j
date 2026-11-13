package ru.yandex.practicum.exceptions;

public class WordNotFoundInDictionaryException extends WordleGameException {
    public WordNotFoundInDictionaryException(String word) {
        super("Слова " + word + " нет в словаре");
    }
}

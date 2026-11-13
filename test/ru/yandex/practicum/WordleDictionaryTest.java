package ru.yandex.practicum;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class WordleDictionaryTest {

    @Test
    @DisplayName("Проверка функции normalize приведения слова к нижнему регистру и замены ё на е")
    void normalizeShouldLowerCaseAndReplaceYo() {
        assertEquals("щелок", WordleDictionary.normalize("ЩЁлоК"));
        assertEquals("герой", WordleDictionary.normalize("Герой"));
    }

    @Test
    @DisplayName("Проверка функции isValidWord валидности слова(5 русских букв)")
    void isValidWordShouldAcceptOnlyFiveRussianLetters() {
        assertTrue(WordleDictionary.isValidWord("герой"));
        assertFalse(WordleDictionary.isValidWord(""), "пустая");
        assertFalse(WordleDictionary.isValidWord("сок"), "слишком короткое");
        assertFalse(WordleDictionary.isValidWord("песочница"), "слишком длинное");
        assertFalse(WordleDictionary.isValidWord("hello"), "латиница");
        assertFalse(WordleDictionary.isValidWord("ха-ха"), "недопустимые элементы(дефис)");
    }
}

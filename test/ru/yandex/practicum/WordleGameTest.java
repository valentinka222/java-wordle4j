package ru.yandex.practicum;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import java.util.Random;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.exceptions.InvalidWordException;
import ru.yandex.practicum.exceptions.WordNotFoundInDictionaryException;


import java.io.PrintWriter;

import static org.junit.jupiter.api.Assertions.*;

class WordleGameTest {
    private static PrintWriter log;
    private WordleDictionary dictionary;
    private WordleGame game;

    @BeforeAll
    static void createLog() {
        log = new PrintWriter(System.out, true);
    }

    @BeforeEach
    void setUp() {
        dictionary = new WordleDictionary(log);
        for (String word : new String[]{"герой", "гонец", "город", "щелок", "полка", "колос", "малыш"}) {
            dictionary.add(word);
        }
        game = new WordleGame(dictionary, "герой", log, new Random(1));
    }

    @Test
    @DisplayName("compare: '+' на своём месте, '^' в другом месте, '-' если буквы нет")
    void compareShouldMarkCorrectMisplacedAndMissingLetters() {
        assertEquals("+++++", WordleGame.compare("герой", "герой"));
        assertEquals("-----", WordleGame.compare("малыш", "герой"));
        assertEquals("+^-^-", WordleGame.compare("гонец", "герой"), "пример из ТЗ");
        assertEquals("^^+++", WordleGame.compare("амлыш", "малыш"), "буквы поменялись местами");
    }

    @Test
    @DisplayName("compare: повторяющиеся буквы отмечаются не чаще, чем встречаются в загаданном слове")
    void compareShouldHandleRepeatedLetters() {
        // в "полка" одна "о": вторая "о" в "колос" лишняя
        assertEquals("^++--", WordleGame.compare("колос", "полка"));
        // в "малыш" одна "а": отмечена только та, что на своём месте
        assertEquals("-+---", WordleGame.compare("ааааа", "малыш"));
    }

    @Test
    @DisplayName("guess: некорректное слово бросает исключение и не тратит попытку")
    void guessShouldThrowAndKeepStepsForInvalidInput() {
        assertThrows(InvalidWordException.class, () -> game.guess("кот"), "неверная длина");
        assertThrows(InvalidWordException.class, () -> game.guess("hello"), "не русские буквы");
        assertThrows(WordNotFoundInDictionaryException.class, () -> game.guess("зззйй"), "нет в словаре");
        assertEquals(WordleGame.MAX_STEPS, game.getSteps());
    }

    @Test
    @DisplayName("guess: верное слово выигрывает игру, а шесть ошибок подряд приводят к проигрышу")
    void guessShouldEndGameWithWinOrLoss() throws Exception {
        assertEquals("+++++", game.guess("герой"));
        assertTrue(game.isWon());
        assertTrue(game.isFinished());

        WordleGame losingGame = new WordleGame(dictionary, "герой", log, new Random(1));
        for (int i = 0; i < WordleGame.MAX_STEPS; i++) {
            assertFalse(losingGame.isFinished());
            losingGame.guess("гонец");
        }
        assertTrue(losingGame.isFinished());
        assertFalse(losingGame.isWon());
        assertEquals(0, losingGame.getSteps());
    }

    @Test
    @DisplayName("hint: подсказка не противоречит прошлым ходам и не теряет загаданное слово")
    void hintShouldFitPreviousAttemptsAndKeepAnswer() throws Exception {
        String result = game.guess("гонец");

        for (int i = 0; i < 20; i++) {
            String hint = game.hint();
            assertEquals(result, WordleGame.compare("гонец", hint), "подсказка противоречит ходу");
            assertNotEquals("гонец", hint, "подсказка повторяет уже введённое слово");
            assertEquals("герой", hint, "из словаря подходит только загаданное слово");
        }
    }
}

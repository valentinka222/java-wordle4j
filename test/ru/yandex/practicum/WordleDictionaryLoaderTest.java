package ru.yandex.practicum;

import org.junit.jupiter.api.*;
import ru.yandex.practicum.exceptions.DictionaryLoadException;
import ru.yandex.practicum.exceptions.EmptyDictionaryException;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

public class WordleDictionaryLoaderTest {
    private static PrintWriter log;
    private WordleDictionaryLoader loader;
    private Path file;

    @BeforeAll
    static void createLog() {
        log = new PrintWriter(System.out, true);
    }

    @BeforeEach
    void setUp() throws IOException {
        loader = new WordleDictionaryLoader(log);
        file = Files.createTempFile("dictionary", ".txt");
    }

    @AfterEach
    void tearDown() throws IOException {
        Files.deleteIfExists(file);
    }

    private void writeLines(String... lines) throws IOException {
        Files.writeString(file, String.join("\r\n", lines), StandardCharsets.UTF_8);
    }

    @Test
    @DisplayName("Загрузчик оставляет только подходящие слова: нормализует их и убирает дубликаты")
    void loadDictionaryShouldKeepOnlyNormalizedUniqueFiveLetterWords() throws IOException {
        writeLines("Герой", "ГЕРОЙ", "Щёлок", "кот", "героиня", "hello", "ля-ля");
        WordleDictionary dictionary = loader.loadDictionary(file.toString());

        assertEquals(2, dictionary.size());
        assertTrue(dictionary.contains("герой"));
        assertTrue(dictionary.contains("щелок"));
    }

    @Test
    @DisplayName("Если файла словаря нет, выбрасывается DictionaryLoadException")
    void loadDictionaryShouldThrowWhenFileIsMissing() {
        assertThrows(DictionaryLoadException.class,
                () -> loader.loadDictionary("no_such_file_12345.txt"));
    }

    @Test
    @DisplayName("Если в файле нет подходящих слов, выбрасывается EmptyDictionaryException")
    void loadDictionaryShouldThrowWhenNoSuitableWords() throws IOException {
        writeLines("кот", "hello", "героиня");
        assertThrows(EmptyDictionaryException.class, () -> loader.loadDictionary(file.toString()));
    }
}

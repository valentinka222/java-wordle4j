package ru.yandex.practicum;

import ru.yandex.practicum.exceptions.DictionaryLoadException;
import ru.yandex.practicum.exceptions.EmptyDictionaryException;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;

public class WordleDictionaryLoader {
    private final PrintWriter log;

    public WordleDictionaryLoader(PrintWriter log) {
        this.log = log;
    }

    public WordleDictionary loadDictionary(String filename) {
        WordleDictionary dict = new WordleDictionary(log);
        int totalLines = 0;
        int duplicates = 0;
        try (BufferedReader br = new BufferedReader(new FileReader(filename,
                StandardCharsets.UTF_8))) {
            String line;
            while ((line = br.readLine()) != null) {
                totalLines++;
                String word = WordleDictionary.normalize(line);
                if (WordleDictionary.isValidWord(word)) {
                    if (!dict.add(word)) {
                        duplicates++;
                    }
                }
            }
        } catch (IOException e) {
            throw new DictionaryLoadException("Не удалось прочитать файл", e);
        }
        log.println("Словарь " + filename + ": прочитано строк " + totalLines + ", подходящих" +
                " слов " + dict.size() + ", дубликатов пропущено " + duplicates);
        if (dict.isEmpty()) {
            throw new EmptyDictionaryException("Файл не содержит подходящих слов");
        }
        return dict;
    }
}

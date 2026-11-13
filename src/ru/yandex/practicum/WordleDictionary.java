package ru.yandex.practicum;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class WordleDictionary {

    private final List<String> words = new ArrayList<>();
    private final Set<String> wordSet = new HashSet<>();
    private final PrintWriter log;

    public WordleDictionary(PrintWriter log) {
        this.log = log;
    }

    public static String normalize(String word) {
        return word.trim().toLowerCase().replace("ё", "е");
    }

    public static boolean isValidWord(String word) {
        if (word.length() != WordleGame.WORD_LENGTH) {
            return false;
        }
        for (int i = 0; i < word.length(); i++) {
            char c = word.charAt(i);
            if (c < 'а' || c > 'я') {
                return false;
            }
        }
        return true;
    }

    boolean add(String word) {
        if (wordSet.add(word)) {
            words.add(word);
            return true;
        }
        log.println("Дубликат в словаре пропущен: " + word);
        return false;
    }

    public String get(int index) {
        return words.get(index);
    }

    public int size() {
        return words.size();
    }

    public boolean contains(String word) {
        return wordSet.contains(word);
    }

    public boolean isEmpty() {
        return words.isEmpty();
    }

    public List<String> getWords() {
        return words;
    }
}

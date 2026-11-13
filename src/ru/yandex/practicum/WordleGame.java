package ru.yandex.practicum;

import ru.yandex.practicum.exceptions.GameStateException;
import ru.yandex.practicum.exceptions.InvalidWordException;
import ru.yandex.practicum.exceptions.WordNotFoundInDictionaryException;

import java.io.PrintWriter;
import java.util.*;

public class WordleGame {
    public static final int WORD_LENGTH = 5;
    public static final int MAX_STEPS = 6;

    private final String answer;
    private int steps = MAX_STEPS;
    private final WordleDictionary dictionary;
    private final List<String> candidates;
    private final Random random;
    private final PrintWriter log;
    private boolean won = false;

    public WordleGame(WordleDictionary dictionary, String answer,
                      PrintWriter log, Random random) {
        if (!dictionary.contains(answer)) {
            throw new GameStateException("Такого слова нет в словаре");
        }
        this.dictionary = dictionary;
        this.answer = answer;
        this.random = random;
        this.log = log;
        this.candidates = new ArrayList<>(dictionary.getWords());
        log.println("Новая игра, загадано: " + answer);
    }

    public String guess(String input) throws InvalidWordException,
            WordNotFoundInDictionaryException {
        if (isFinished()) {
            throw new GameStateException("Ход после окончания игры");
        }
        String word = WordleDictionary.normalize(input);
        if (!WordleDictionary.isValidWord(word)) {
            throw new InvalidWordException("Нужно слово из пяти русских букв");
        }
        if (!dictionary.contains(word)) {
            throw new WordNotFoundInDictionaryException(word);
        }

        String result = compare(word, answer);
        steps--;
        if (word.equals(answer)) {
            won = true;
        }

        candidates.removeIf(candidate -> !compare(word, candidate).equals(result));
        if (!candidates.contains(answer)) {
            throw new GameStateException("Загаданное слово пропало из вариантов после хода " + word);
        }

        log.println("Ход: " + word + ", результат: " + result + ", осталось шагов: " + steps);
        return result;
    }

    public static String compare(String inputWord, String hiddenWord) {
        Map<Character, Integer> freeLetters = new HashMap<>();
        StringBuilder result = new StringBuilder("-".repeat(WORD_LENGTH));

        for (int i = 0; i < WORD_LENGTH; i++) {
            if (hiddenWord.charAt(i) == inputWord.charAt(i)) {
                result.setCharAt(i, '+');
            } else {
                char a = hiddenWord.charAt(i);
                int count = freeLetters.getOrDefault(a, 0);
                freeLetters.put(a, count + 1);
            }
        }
        for (int i = 0; i < WORD_LENGTH; i++) {
            if (result.charAt(i) == '+') {
                continue;
            }
            char c = inputWord.charAt(i);
            int count = freeLetters.getOrDefault(c, 0);
            if (count > 0) {
                result.setCharAt(i, '^');
                freeLetters.put(c, count - 1);
            }
        }
        return result.toString();
    }

    public String hint() {
        if (candidates.isEmpty()) {
            throw new GameStateException("Нет подходящих слов для подсказки");
        }
        String hint = candidates.get(random.nextInt(candidates.size()));
        log.println("Подсказка: " + hint);
        return hint;
    }

    public int getSteps() {
        return steps;
    }

    public String getAnswer() {
        return answer;
    }

    public boolean isWon() {
        return won;
    }

    public boolean isFinished() {
        return isWon() || steps <= 0;
    }

}

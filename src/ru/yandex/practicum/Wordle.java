package ru.yandex.practicum;

import ru.yandex.practicum.exceptions.WordleGameException;
import ru.yandex.practicum.exceptions.WordleSystemException;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.Random;
import java.util.Scanner;

public class Wordle {
    private static final String DICTIONARY_FILE = "words_ru.txt";
    private static final String LOG_FILE = "wordle.log";

    public static void main(String[] args) {
        try (PrintWriter log = new PrintWriter(new FileWriter(LOG_FILE, StandardCharsets.UTF_8))){
            try {
                run(log);
            } catch (WordleSystemException e) {
                log.println("Системная ошибка: " + e.getMessage());
                e.printStackTrace(log);
                System.out.println("Игра завершается из-за системной ошибки. " +
                        "Подробности в " + LOG_FILE);
            } catch (Exception e) {
                log.println("Непредвиденная ошибка: " + e);
                e.printStackTrace(log);
                System.out.println("Игра завершается из-за непредвиденной ошибки. " +
                        "Подробности в " + LOG_FILE);
            }
        } catch (IOException e) {
            System.out.println("Не удалось создать лог-файл: " + e.getMessage());
        }
    }

    public static void run(PrintWriter log) {
        Random random = new Random();
        Scanner scanner = new Scanner(System.in);

        WordleDictionary dictionary = new WordleDictionaryLoader(log).loadDictionary(DICTIONARY_FILE);
        String answer = dictionary.get(random.nextInt(dictionary.size()));
        WordleGame game = new WordleGame(dictionary, answer, log, random);
        System.out.println("Компьютер загадал слово. У вас " + WordleGame.MAX_STEPS + " попыток. " +
                "Пустая строка (Enter) - подсказка.");
        while (!game.isFinished()) {
            System.out.print("Введите слово: ");
            String input = scanner.nextLine();

            if (input.isBlank()) {
                input = game.hint();
                System.out.println("Подсказка: " + input);
            }

            try {
                System.out.println(game.guess(input));
                System.out.println("Осталось попыток: " + game.getSteps());
            } catch (WordleGameException e) {
                System.out.println(e.getMessage() + ". Попытка не засчитана");
                log.println("Некорректный ввод (" + input + "): " + e);
            }
        }
        if (game.isWon()) {
            System.out.println("Вы выиграли. Поздравляю!");
        } else {
            System.out.println("Вы проиграли. Не расстраивайтесь :(");
        }
        System.out.println("Загаданное слово: " + game.getAnswer());
    }
}

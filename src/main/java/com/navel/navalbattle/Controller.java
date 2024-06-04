package com.navel.navalbattle;

import java.io.*;
import java.util.Properties;

public class Controller {
    protected int fieldSpots;
    protected int fieldSize;
    protected int squareSize;

    /**
     * Виконує зчитання з файлу основниї даних, які необхідні для роботи гри.
     */
    public Controller() {
        File gameData = new File("src/main/resources/data/game_data.properties");
        if (!gameData.exists()) {
            createDefaultGameData(gameData);
        }

        Properties properties = new Properties();
        try (FileInputStream fis = new FileInputStream("src/main/resources/data/game_data.properties")) {
            properties.load(fis);
        } catch (IOException e) {
            System.out.println("Помилка при читанні game_data.properties в конструкторі класу Controller.");
        }

        try {
            fieldSize = Integer.parseInt(properties.getProperty("fieldSize"));
            fieldSpots = Integer.parseInt(properties.getProperty("fieldSpots"));
        } catch (NumberFormatException e) {
            System.out.println("Значення в файлі game_data.properties не є коректними. Виконується їх перезапис значеннями за замовчуванням.");
            createDefaultGameData(gameData);
        }

        squareSize = fieldSize / fieldSpots;
    }

    /**
     * У випадку проблем із файлом даних, створює новий із значеннями за замовчуванням.
     * @param file Шлях за яким буде створено новий файл.
     */
    private void createDefaultGameData(File file) {
        if (file == null)
            throw new IllegalArgumentException("file не може бути null в методі createDefaultGameData класу Controller.");

        Properties properties = new Properties();
        properties.setProperty("fieldSize", "400");
        properties.setProperty("fieldSpots", "10");

        try (OutputStream outputStream = new FileOutputStream(file)) {
            properties.store(outputStream, null);
        } catch (IOException e) {
            throw new RuntimeException("Помилка при запису в файл в методі createDefaultGameData класу Controller.");
        }
    }
}

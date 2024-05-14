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
            System.out.println("An error occurred while reading game_data.properties file.");
            e.printStackTrace();
        }

        try {
            fieldSize = Integer.parseInt(properties.getProperty("fieldSize"));
            fieldSpots = Integer.parseInt(properties.getProperty("fieldSpots"));
        } catch (NumberFormatException e) {
            System.out.println("game_data.properties file values are corrupted, overwriting it with default values.");
            createDefaultGameData(gameData);
            e.printStackTrace();
        }

        squareSize = fieldSize / fieldSpots;
    }

    /**
     * У випадку проблем із файлом даних, створює новий із значеннями за замовчуванням.
     * @param file Шлях за яким буде створено новий файл.
     */
    private void createDefaultGameData(File file) {
        Properties properties = new Properties();
        properties.setProperty("fieldSize", "400");
        properties.setProperty("fieldSpots", "10");

        try (OutputStream outputStream = new FileOutputStream(file)) {
            properties.store(outputStream, null);
        } catch (IOException e) {
            System.out.println("An error occurred while creating default game_data.properties file.");
            e.printStackTrace();
        }
    }
}

package com.navel.navalbattle.database;

import java.net.ConnectException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnector {
    private static final String URL = "jdbc:postgresql://26.181.80.75:5432/navalbattle";
    private static final String user = "navalbattle";
    private static final String password = "navalbattle";
    private static Connection connection = null;
    private static int userId;

    /**
     * Виконує з'єднання з базою даних.
     * @return true - якщо з'єднання вдале, false - якщо ні.
     */
    public static boolean makeConnection() {
        try {
            connection = DriverManager.getConnection(URL, user, password);
            return true;
        } catch (SQLException e) {
            Throwable cause = e.getCause();
            if (cause instanceof ConnectException) {
                System.out.println("Database is offline");
                return false;
            }
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Закриває з'єднання з базою даних.
     */
    public static void closeConnection() {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Повертає об'єкт з'єднання з базою даних.
     * @return об'єкт з'єднання з базою даних.
     */
    public static Connection getConnection() {
        return connection;
    }

    /**
     * Встановлює ідентифікатор користувача.
     * @param userId ідентифікатор користувача.
     */
    public static void setUserId(int userId) {
        DatabaseConnector.userId = userId;
    }

    /**
     * Повертає ідентифікатор користувача.
     * @return ідентифікатор користувача.
     */
    public static int getUserId() {
        return userId;
    }
}

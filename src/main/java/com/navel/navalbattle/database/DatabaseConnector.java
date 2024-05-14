package com.navel.navalbattle.database;

import java.net.ConnectException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnector {
//    private static final String INSTANCE_CONNECTION_NAME =
//            "autonomous-gist-420818:europe-west3:naval-battle";
//    private static final String DB_USER = "player";
//    private static final String DB_PASS = "12345";
//    private static final String DB_NAME = "postgres";
//    private static Connection connection;
//    private static int userId;

//    public static boolean makeConnection() {
//        try {
//            HikariConfig config = new HikariConfig();
//            config.setJdbcUrl(String.format("jdbc:postgresql:///%s", DB_NAME));
//            config.setUsername(DB_USER);
//            config.setPassword(DB_PASS);
//            config.addDataSourceProperty("socketFactory", "com.google.cloud.sql.postgres.SocketFactory");
//            config.addDataSourceProperty("cloudSqlInstance", INSTANCE_CONNECTION_NAME);
//
//            connection = new HikariDataSource(config).getConnection();
//
//            return true;
//        }
//        catch (SQLException e) {
//            Throwable cause = e.getCause();
//            if (cause instanceof ConnectException) {
//                System.out.println("Database is offline");
//                return false;
//            }
//            e.printStackTrace();
//            return false;
//        }
//    }

    private static final String URL = "jdbc:postgresql://26.181.80.75:5432/navalbattle";
    private static final String user = "navalbattle";
    private static final String password = "navalbattle";
    private static Connection connection = null;
    private static int userId;

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

    public static void closeConnection() {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static Connection getConnection() {
        return connection;
    }

    public static void setUserId(int userId) {
        DatabaseConnector.userId = userId;
    }

    public static int getUserId() {
        return userId;
    }
}

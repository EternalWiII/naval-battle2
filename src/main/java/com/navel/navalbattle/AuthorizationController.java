package com.navel.navalbattle;

import com.navel.navalbattle.database.DatabaseConnector;
import com.navel.navalbattle.interfaces.WindowsManipulations;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AuthorizationController extends Controller implements WindowsManipulations {
    @FXML
    private TextField usernameField;
    @FXML
    private TextField passwordField;

    /**
     * Виконується при натисканні на кнопку "Увійти".
     * Перевіряє введені дані та авторизує користувача.
     *
     * @param e Подія натискання на кнопку.
     */
    @FXML
    private void onLoginBtnPress(ActionEvent e) {
        if (!checkInputFields()) {
            return;
        }

        String query = "SELECT id\n" +
                        "FROM accounts\n" +
                        "WHERE username = ? AND password = ?;";
        PreparedStatement statement = null;

        try {
            statement = DatabaseConnector.getConnection().prepareStatement(query);
            statement.setString(1, usernameField.getText());
            statement.setString(2, passwordField.getText());
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                DatabaseConnector.setUserId(resultSet.getInt(1));

                FXMLLoader loader = new FXMLLoader(getClass().getResource("main_menu.fxml"));
                Scene scene = new Scene(loader.load());

                Stage stage = (Stage) usernameField.getScene().getWindow();

                stage.setScene(scene);

                scene.setOnKeyPressed(event -> {
                    event.consume();

                    if (event.getCode() == KeyCode.ESCAPE) {
                        processExit(stage);
                    }
                });
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR, "You are not authorized", ButtonType.OK);
                alert.showAndWait();
            }
        } catch (SQLException ex) {
            throw new RuntimeException("Помилка при роботі з базою даних в методі onLoginBtnPress класу AuthorizationController.");
        } catch (IOException ex) {
            throw new RuntimeException("Помилка при читанні fxml файлу в методі onLoginBtnPress класу AuthorizationController.");
        }
    }

    /**
     * Виконується при натисканні на кнопку "Зареєструватися".
     * Перевіряє введені дані та реєструє користувача.
     */
    @FXML
    private void onRegisterBtnPress() {
        if (!checkInputFields()) {
            return;
        }

        try {
            String query = "INSERT INTO accounts (username, password) VALUES (?, ?);";
            PreparedStatement statement = DatabaseConnector.getConnection().prepareStatement(query);
            statement.setString(1, usernameField.getText());
            statement.setString(2, passwordField.getText());

            statement.executeUpdate();

            query = "SELECT id\n" +
                    "FROM accounts\n" +
                    "WHERE username = ? AND password = ?;";
            statement = DatabaseConnector.getConnection().prepareStatement(query);
            statement.setString(1, usernameField.getText());
            statement.setString(2, passwordField.getText());
            statement.execute();

            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            DatabaseConnector.setUserId(resultSet.getInt(1));

            FXMLLoader loader = new FXMLLoader(getClass().getResource("main_menu.fxml"));
            Scene scene = new Scene(loader.load());

            Stage stage = (Stage) usernameField.getScene().getWindow();

            stage.setScene(scene);

            scene.setOnKeyPressed(event -> {
                event.consume();

                if (event.getCode() == KeyCode.ESCAPE) {
                    processExit(stage);
                }
            });
        } catch (SQLException e) {
            if (e.getSQLState().equals("23505")) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Registration failed", ButtonType.OK);
                alert.setContentText("This account name has already been taken.");
                alert.showAndWait();
            } else {
                throw new RuntimeException("Помилка при роботі з базою даних в методі onRegisterBtnPress класу AuthorizationController.");
            }
        } catch (IOException ex) {
            throw new RuntimeException("Помилка при читанні fxml файлу в методі onRegisterBtnPress класу AuthorizationController.");
        }
    }

    /**
     * Перевіряє чи всі поля введені коректно.
     *
     * @return true якщо всі поля заповнені, false якщо хоча б одне поле не заповнене.
     */
    private boolean checkInputFields() {
        if (usernameField.getText().trim().isEmpty() || passwordField.getText().trim().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Fill in all fields", ButtonType.OK);
            alert.showAndWait();

            return false;
        }
        return true;
    }
}

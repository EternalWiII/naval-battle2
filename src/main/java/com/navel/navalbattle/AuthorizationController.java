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
    private Connection connection;
    @FXML
    private TextField usernameField;
    @FXML
    private TextField passwordField;

    @FXML
    private void onLoginBtnPress(ActionEvent e) throws SQLException, IOException {
        if (!checkInputFields()) {
            return;
        }

        String query = "SELECT id\n" +
                        "FROM accounts\n" +
                        "WHERE username = ? AND password = ?;";
        PreparedStatement statement = DatabaseConnector.getConnection().prepareStatement(query);
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
    }

    @FXML
    private void onRegisterBtnPress() throws SQLException, IOException {
        if (!checkInputFields()) {
            return;
        }

        String query = "INSERT INTO accounts (username, password) VALUES (?, ?);";
        PreparedStatement statement = DatabaseConnector.getConnection().prepareStatement(query);
        statement.setString(1, usernameField.getText());
        statement.setString(2, passwordField.getText());

        try {
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
            Alert alert = new Alert(Alert.AlertType.ERROR, "Registration failed", ButtonType.OK);
            alert.setContentText("This account name has already been taken.");
            alert.showAndWait();
        }
    }

    private boolean checkInputFields() {
        if (usernameField.getText().isEmpty() || passwordField.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Fill in all fields", ButtonType.OK);
            alert.showAndWait();

            return false;
        }

        return true;
    }
}

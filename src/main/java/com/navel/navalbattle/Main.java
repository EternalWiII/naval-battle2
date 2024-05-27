package com.navel.navalbattle;

import com.navel.navalbattle.database.DatabaseConnector;
import com.navel.navalbattle.interfaces.WindowsManipulations;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import javafx.scene.image.Image;

import java.awt.*;
import java.awt.image.ImageObserver;
import java.awt.image.ImageProducer;
import java.io.IOException;
import java.sql.SQLException;

public class Main extends Application implements WindowsManipulations {
    /**
     * Отримує поточний stage та завантажує сцену головного меню.
     * @param stage Поточний stage.
     * @throws IOException Помилка при читанні fxml файлу.
     */
    @Override
    public void start(Stage stage) throws IOException {
        if(!DatabaseConnector.makeConnection()) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Can't connect to database", ButtonType.OK);
            alert.setContentText("You will continue without saving your statistics. If you want to save it, please, restart the game.");
            alert.showAndWait();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("main_menu.fxml"));
            Scene scene = new Scene(loader.load());

            Image icon = new Image(getClass().getResourceAsStream("/images/favicon.png"));
            stage.getIcons().add(icon);
            stage.setResizable(false);

            MainMenuController controller = (MainMenuController)loader.getController();

            stage.setScene(scene);
            stage.show();

            stage.setOnCloseRequest(event -> {
                        event.consume();
                        processExit(stage);
                    }
            );

            scene.setOnKeyPressed(event -> {
                event.consume();

                if (event.getCode() == KeyCode.ESCAPE) {
                    processExit(stage);
                }
            });
        }
        else {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("authorization.fxml"));
            Scene scene = new Scene(loader.load());
            AuthorizationController controller = loader.getController();

            stage.setTitle("Naval battle");
            stage.setResizable(false);

            stage.setScene(scene);
            stage.show();

            stage.setOnCloseRequest(event -> {
                        event.consume();
                        processExit(stage);
                    }
            );

            scene.setOnKeyPressed(event -> {
                event.consume();

                if (event.getCode() == KeyCode.ESCAPE) {
                    processExit(stage);
                }
            });
        }
    }

    /**
     * Запуск програми.
     * @param args Додаткові параметри.
     */
    public static void main(String[] args) {
        launch();
    }
}
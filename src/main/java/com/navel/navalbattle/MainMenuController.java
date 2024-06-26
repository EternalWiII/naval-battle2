package com.navel.navalbattle;

import com.navel.navalbattle.database.DatabaseConnector;
import com.navel.navalbattle.interfaces.WindowsManipulations;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

public class MainMenuController extends Controller implements WindowsManipulations {
    private Stage stage;

    @FXML
    private Pane scenePane;
    @FXML
    private VBox vBox;
    @FXML
    private ImageView imageView;

    /**
     * Ініціалізує розміри та розташування елементів на сцені.
     */
    @FXML
    private void initialize() {
        vBox.prefWidthProperty().bind(scenePane.widthProperty().subtract(200));
        vBox.prefHeightProperty().bind(scenePane.heightProperty().subtract(200));

        vBox.spacingProperty().bind(scenePane.heightProperty().divide(5));

        imageView.setPreserveRatio(true);
    }

    /**
     * Завантажує наступну сцену з розташуванням кораблів та події для неї.
     * @param e Подія натискання на кнопку.
     */
    @FXML
    protected void onStartGameClick(ActionEvent e) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ships_positioning.fxml"));
            Scene scene = new Scene(loader.load());

            ShipsPositioningController controller = loader.getController();

            stage = (Stage) ((Node) e.getSource()).getScene().getWindow();;

            stage.setScene(scene);
            stage.show();

            scene.setOnKeyPressed(event -> {
                event.consume();

                if (event.getCode() == KeyCode.ESCAPE) {
                    processReturnToMainMenu(stage);
                }

                if (event.getCode() == KeyCode.R) {
                    controller.setRPressed();
                }
            });
        } catch (IOException ex) {
            throw new RuntimeException("Помилка при читанні файлу ships_positioning.fxml в методі onStartGameClick класу MainMenuController.", ex);
        }
    }

    /**
     * Оброблює натискання на кнопку виходу.
     */
    @FXML
    protected void onExitClick(ActionEvent e) {
        stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        processExit(stage);
    }

    /**
     * Завантажує наступну сцену зі статистикою гравця та події для неї.
     *
     * @param e Подія натискання на кнопку.
     */
    @FXML
    protected void onStatClick(ActionEvent e) {
        try {
            if (DatabaseConnector.getConnection() != null) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("statistics.fxml"));
                Scene scene = new Scene(loader.load());

                stage = (Stage) ((Node) e.getSource()).getScene().getWindow();

                stage.setScene(scene);
                stage.show();

                scene.setOnKeyPressed(event -> {
                    event.consume();

                    if (event.getCode() == KeyCode.ESCAPE) {
                        processReturnToMainMenu(stage);
                    }
                });
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Connect online to see statistics");

                alert.showAndWait();
            }
        } catch (IOException ex) {
            throw new RuntimeException("Помилка при читанні файлу statistics.fxml в методі onStatClick класу MainMenuController.", ex);
        }
    }
}
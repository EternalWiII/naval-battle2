package com.navel.navalbattle;

import com.navel.navalbattle.interfaces.GridCalculations;
import com.navel.navalbattle.interfaces.WindowsManipulations;
import com.navel.navalbattle.ships.*;
import com.navel.navalbattle.records.GridPosition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.io.IOException;

public class ShipsPositioningController extends Controller implements GridCalculations, WindowsManipulations {
    private Stage stage;
    @FXML
    private Pane fieldPane;
    private int shipStartX = fieldSize + squareSize;
    private int shipStartY = 0;
    private Ship[] shipArr;
    private boolean isDragged = false;
    private Ship draggedShip;

    /**
     * Вкионується при ініціалізації відповідного контроллера, створює та розташовує кораблі користувача.
     */
    @FXML
    public void initialize() {
        shipArr = new Ship[10];

        createShips(shipArr, fieldPane, squareSize, shipStartX, shipStartY, true);
        addPositioningEvents(shipArr);
    }

    /**
     * Додає до кораблів користувача події, які відповідають за їх переміщення.
     * @param shipArr Масив із кораблями користувача.
     */
    private void addPositioningEvents(Ship[] shipArr) {
        for (Ship ship : shipArr) {
            ship.getRec().setOnMousePressed(event -> recPressed(event, ship));
            ship.getRec().setOnMouseDragged(event -> recDragged(event, ship));
            ship.getRec().setOnMouseReleased(event -> recReleased(event, ship));
        }
    }

    /**
     * Оброблює момент натискання лівою кнопкою миші на корабель.
     * @param event Подія миші.
     * @param s Корабель, із якми відбуваєтсья подія.
     */
    private void recPressed(MouseEvent event, Ship s) {
        s.setHomeX(s.getX());
        s.setHomeY(s.getY());
        s.setHomeIsVertical(s.isVertical());
        draggedShip = s;
    }

    /**
     * Оброблює момент перетягування лівою кнопкою миші корабля.
     * @param event Подія миші.
     * @param s Корабель, із якми відбуваєтсья подія.
     */
    private void recDragged(MouseEvent event, Ship s) {
        isDragged = true;

        s.getRec().toFront();

        s.setX((event.getSceneX()) - (double)(squareSize * s.getShipSize()) / 2 - 20);
        s.setY((event.getSceneY()) - (double)(squareSize) / 2 - 70);

        s.draw();
    }

    /**
     * Оброблює момент відпускання лівою кнопкою миші корабля.
     * @param event Подія миші.
     * @param s Корабель, із якми відбуваєтсья подія.
     */
    private void recReleased(MouseEvent event, Ship s) {
        isDragged = false;

        GridPosition position = getPosition(s, squareSize);

        if (canPlace(s, shipArr, position)) {
            if (s.isVertical()) {
                s.setX(squareSize * position.x() - s.getOffset());
                s.setY(squareSize * position.y() + s.getOffset());
            }
            else {
                s.setX(squareSize * position.x());
                s.setY(squareSize * position.y());
            }
            s.draw();
        }
        else {
            s.setX(s.getHomeX());
            s.setY(s.getHomeY());
            if (s.isVertical() != s.isHomeIsVertical()) {
                s.flipIsVertical();
            }
            s.draw();
        }
    }

    /**
     * Цей метод викликається подією, яка спрацьовує при натисканні кнопки 'R'. Виконує поворот для корабля, який тримає користувач.
     */
    public void setRPressed() {
        if(isDragged) {
            draggedShip.flipIsVertical();
        }
    }

    /**
     * Завантажує наступну сцену гри та події для неї.
     * @param e Подія натискання на кнопку.
     * @throws IOException Помилка при читанні fxml файлу.
     */
    @FXML
    protected void onStartClick(ActionEvent e) throws IOException, InterruptedException {
        if (checkShipsPlaced()) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("game.fxml"));
            Scene scene = new Scene(loader.load());
            GameController controller = loader.getController();
            controller.setPlayerShipArr(shipArr);

            stage = (Stage) ((Node) e.getSource()).getScene().getWindow();

            stage.setScene(scene);
            stage.show();

            scene.setOnKeyPressed(event -> {
                event.consume();

                if (event.getCode() == KeyCode.ESCAPE) {
                    processReturnToMain(stage);
                }
            });

            controller.startGame();
        }
        else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.showAndWait();
        }
    }

    /**
     * Перевіряє чи розташував користувач всі свої кораблі.
     * @return true якщо всі коряблі стоять коректно, false якщо користувач розташував ще не всі кораблі.
     */
    protected boolean checkShipsPlaced() {
        for (int i = 0; i < 10; i++) {
            if (shipArr[i].isVertical()) {
                if (shipArr[i].getX() + shipArr[i].getOffset() >= 400) {
                    return false;
                }
            }
            else {
                if (shipArr[i].getX() >= 400) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Оброблює натискання на кнопку очищення ігрового поля. Відповідно, повертає всі кораблі на свої початкові позиції поза ігровим полем.
     * @param e Подія натискання на кнопку.
     */
    @FXML
    protected void onClearFieldClick(ActionEvent e) {
        int newY = 0;
        for (int i = 0; i < 10; i++) {
            shipArr[i].setX(fieldSize + squareSize);
            shipArr[i].setY(newY);
            if (shipArr[i].isVertical()) {
                shipArr[i].flipIsVertical();
            }
            newY += squareSize;
            shipArr[i].draw();
        }
    }

    /**
     * Оброблює натискання на кнопку авторозташування кораблів. Відповідно, автоматично розташовує всі кораблі користувача на ігровому полі із урахуванням всіх правил.
     * @param e Подія натискання на кнопку.
     */
    @FXML
    protected void onAutoplaceClick(ActionEvent e) {
        onClearFieldClick(e);

        autoplaceShips(shipArr, squareSize, fieldSpots);
    }
}

package com.navel.navalbattle.ships;

import com.navel.navalbattle.records.ShipUsedArea;
import javafx.animation.FadeTransition;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

public class Ship {
    private static int numberOfShips = 0;
    protected int shipID;
    protected int shipSize;
    protected int squareSize;
    protected Rectangle rec;
    protected double x;
    protected double y;
    protected boolean isVertical = false;
    protected double homeX;
    protected double homeY;
    protected boolean homeIsVertical;
    protected double offset = 0;
    protected int hp;
    protected Image shipImage;

    /**
     * Конструктор об'єкта Ship, який ініціалізує основні параметри.
     * @param shipID Порядковий номер корабля.
     * @param squareSize Довжина однієї сторони клітини в пікселях.
     * @param x Координата X корабля.
     * @param y Координата Y корабля.
     */
    public Ship(int shipID, int squareSize, int x, int y) {
        this.squareSize = squareSize;
        rec = new Rectangle();
        this.x = x;
        this.y = y;
        this.shipID = shipID;

        numberOfShips++;
    }

    /**
     * Повертає статичне поле numberOfShips.
     * @return Статичне поле numberOfShips.
     */
    public static int getNumberOfShips() {
        return numberOfShips;
    }

    /**
     * Гетер для поля homeX.
     * @return поле homeX.
     */
    public double getHomeX() {
        return homeX;
    }

    /**
     * Сетер для поля homeX.
     * @param homeX Нове значення координати homeX.
     */
    public void setHomeX(double homeX) {
        this.homeX = homeX;
    }

    /**
     * Гетер для поля homeX.
     * @return поле homeX.
     */
    public double getHomeY() {
        return homeY;
    }

    /**
     * Сетер для поля homeY.
     * @param homeY Нове значення координати homeY.
     */
    public void setHomeY(double homeY) {
        this.homeY = homeY;
    }

    /**
     * Гетер для поля x.
     * @return поле x.
     */
    public double getX() {
        return x;
    }

    /**
     * Сетер для поля x.
     * @param x Нове значення координати x.
     */
    public void setX(double x) {
        this.x = x;
    }

    /**
     * Гетер для поля y.
     * @return поле y.
     */
    public double getY() {
        return y;
    }

    /**
     * Сетер для поля y.
     * @param y Нове значення координати y.
     */
    public void setY(double y) {
        this.y = y;
    }

    /**
     * Гетер для поля isVertical.
     * @return поле isVertical.
     */
    public boolean isVertical() {
        return isVertical;
    }

    /**
     * Гетер для поля homeIsVertical.
     * @return поле homeIsVertical.
     */
    public boolean isHomeIsVertical() {
        return homeIsVertical;
    }

    /**
     * Сетер для поля homeIsVertical.
     * @param homeIsVertical Нове значення homeIsVertical.
     */
    public void setHomeIsVertical(boolean homeIsVertical) {
        this.homeIsVertical = homeIsVertical;
    }

    /**
     * Гетер для поля offset.
     * @return поле offset.
     */
    public double getOffset() {
        return offset;
    }

    /**
     * Гетер для поля shipSize.
     * @return поле shipSize.
     */
    public int getShipSize() {
        return shipSize;
    }

    /**
     * Гетер для поля rec.
     * @return поле rec.
     */
    public Rectangle getRec() {
        return rec;
    }

    /**
     * Гетер для поля shipID.
     * @return поле shipID.
     */
    public int getShipID() {
        return shipID;
    }

    /**
     * Сетер для поля squareSize.
     * @param squareSize Нове значення squareSize.
     */
    public void setSquareSize(int squareSize) {
        this.squareSize = squareSize;
    }

    /**
     * Відмальовує пямокутник корябля на поточних координатах.
     */
    public void draw() {
        rec.setTranslateX(x);
        rec.setTranslateY(y);
    }

    /**
     * Виконує зміну стану корабля (вертикальний або горизонтальний) на протележний.
     */
    public void flipIsVertical() {
        if (isVertical) {
            isVertical = false;
            rec.setRotate(0);
            draw();
        }
        else {
            isVertical = true;
            rec.setRotate(90);
            draw();
        }
    }

    /**
     * Знаходить область клітин панелі, які зайняті кораблем.
     * @return об'єкт ShipUsedArea із результатом обрахувань.
     */
    public ShipUsedArea getUsedArea() {
        int[] area = new int[4];
        if (isVertical) {
            if (((int)(x) + (int)offset ) / squareSize >= 10) {
                System.out.println(((int)(x) + (int)offset / squareSize));
                area[0] = ((int)(x) + (int)offset) / squareSize;
                area[1] = area[0];

                area[2] = ((int)(y) - (int)offset) / squareSize;
                area[3] = area[2] + shipSize - 1;
            }
            else {
                area[0] = ((int)(x) + (int)offset) / squareSize - 1;
                area[1] = area[0] + 2;

                area[2] = ((int)(y) - (int)offset) / squareSize - 1;
                area[3] = area[2] + shipSize + 1;
            }
        }
        else {
            if (((int)(x) / squareSize) >= 10) {
                area[0] = (int)(x) / squareSize;
                area[1] = area[0] + shipSize - 1;

                area[2] = (int)(y) / squareSize;
                area[3] = area[2];
            }
            else {
                area[0] = (int)(x) / squareSize - 1;
                area[1] = area[0] + shipSize + 1;

                area[2] = (int)(y) / squareSize - 1;
                area[3] = area[2] + 2;
            }
        }

        return new ShipUsedArea(area[0], area[1], area[2] , area[3]);
    }

    /**
     * Зменшує здоров'я корабля та перевіряє чи не зменшилося воно до 0.
     * @return true, якщо здоров'я тепер дорівнює 0, false, якщо в корабля ще  є здоров'я.
     */
    public boolean getHit() {
        hp--;
        return hp == 0;
    }

    /**
     * Візуально змінює корабель до знищеного стану.
     */
    public void becomeVisible() {
        FadeTransition ft = new FadeTransition(Duration.millis(500), rec);
        ft.setFromValue(0.0);
        ft.setToValue(1.0);
        ft.play();
    }

    public double getRecOpacity() {
        return rec.getOpacity();
    }
}
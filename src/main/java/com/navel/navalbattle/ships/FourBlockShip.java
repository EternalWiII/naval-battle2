package com.navel.navalbattle.ships;

import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;

public class FourBlockShip extends Ship {
    /**
     * Конструктор об'єкта FourBlocksShip, який ініціалізує основні параметри за допомгою батьківського конструктора
     * та задає додаткові параметри.
     * @param shipID Порядковий номер корабля.
     * @param squareSize Довжина однієї сторони клітини в пікселях.
     * @param x Координата X корабля.
     * @param y Координата Y корабля.
     */
    public FourBlockShip(int shipID, int squareSize, int x, int y, boolean isVisible) {
        super(shipID, squareSize, x, y);
        if (isVisible) {
            rec.setOpacity(1);
        } else {
            rec.setOpacity(0);
        }
        shipSize = 4;
        hp = shipSize;
        offset = ((double)squareSize * 3) / 2;
        rec.setWidth(squareSize * shipSize);
        rec.setHeight(squareSize);
        shipImage = new Image(getClass().getResourceAsStream("/images/fourBlockShip.png"));
        rec.setFill(new ImagePattern(shipImage));
    }
}

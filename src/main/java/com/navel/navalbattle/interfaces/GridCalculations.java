package com.navel.navalbattle.interfaces;

import com.navel.navalbattle.records.GridPosition;
import com.navel.navalbattle.records.ShipUsedArea;
import com.navel.navalbattle.ships.*;
import javafx.scene.layout.Pane;

import java.util.Random;

public interface GridCalculations {
    /**
     * getPosition конвертує координати в номера рядків і стовпців на панелі.
     * @param s Корабель, координати якого будуть використовуватися.
     * @param squareSize Довжина однієї сторони клітини в пікселях.
     * @return об'єкт GridPosition із результатом обрахувань.
     */
    default GridPosition getPosition(Ship s, int squareSize) {
        int gridx;
        int gridy;

        if (s.isVertical()) {
            gridx = ((int) s.getX() + (int) s.getOffset() + 20) / squareSize;

            if (gridx < 0) {
                gridx = 0;
            }
            if (gridx >= 15) {
                gridx = 14;
            }
            if (gridx == 10) {
                gridx = 11;
            }

            gridy = ((int) s.getY() - (int) s.getOffset() + 20) / squareSize;

            if (gridy < 0) {
                gridy = 0;
            }
            if (gridy >= 10) {
                gridy = 9;
            }
            if ( gridy + s.getShipSize() >= 10) {
                gridy = 10 - s.getShipSize();
            }
        }
        else {
            gridx = ((int)s.getX() + 20) / squareSize;

            if (gridx < 0) {
                gridx = 0;
            }
            if (gridx >= 15) {
                gridx = 14;
            }
            if (gridx == 10) {
                gridx = 11;
            }
            if ( gridx + s.getShipSize() - 1 >= 10 && gridx < 10) {
                gridx = 10 - s.getShipSize();
            }
            if (gridx + s.getShipSize() >= 15 && gridx >=11) {
                gridx = 15 - s.getShipSize();
            }

            gridy = ((int) s.getY() + 20) / squareSize;

            if (gridy < 0) {
                gridy = 0;
            }
            if (gridy >= 10) {
                gridy = 9;
            }
        }

        return new GridPosition(gridx, gridy);
    }

    /**
     * getPosition конвертує координати в номера рядків і стовпців на панелі.
     * @param x Координата X.
     * @param y Координата Y.
     * @param squareSize Довжина однієї сторони клітини в пікселях.
     * @return об'єкт GridPosition із результатом обрахувань.
     */
    default GridPosition getPosition(double x, double y, int squareSize) {
        int gridx;
        int gridy;

        gridx = ((int) x) / squareSize;
        gridy = ((int) y) / squareSize;

        return new GridPosition(gridx, gridy);
    }

    /**
     * createShips приєднує прямокутники кораблів до потрібної панелі та розташовує їх один під одним на певних координатах.
     * @param shipArr Масив кораблей для яких виконується маніпуляції.
     * @param fieldPane Панель, до якої потрібно приєднати прямокутники кораблі.
     * @param squareSize Довжина однієї сторони клітини в пікселях.
     * @param shipStartX Координата X від заданої fieldPane, на якій відбувається розташування.
     * @param shipStartY Координата Y від заданої fieldPane, на якій відбувається розташування.
     */
    default void createShips (Ship[] shipArr, Pane fieldPane, int squareSize, int shipStartX, int shipStartY, boolean makeVisible) {
        for (int i = 0; i < 10; i++) {
            shipArr[i] =
                    switch (i) {
                        case 0 -> new FourBlockShip(i, squareSize, shipStartX, shipStartY, makeVisible);
                        case 1, 2 -> new ThreeBlockShip(i, squareSize, shipStartX, shipStartY, makeVisible);
                        case 3, 4, 5 -> new TwoBlockShip(i, squareSize, shipStartX, shipStartY, makeVisible);
                        default -> new OneBlockShip(i, squareSize, shipStartX, shipStartY, makeVisible);
                    };

            fieldPane.getChildren().add(shipArr[i].getRec());

            shipArr[i].draw();

            shipStartY += 40;
        }
    }

    /**
     * canPlace перевіряє чи можна поставити заданий корабель в певну позицію, чи не буде він заважати іншим кораблям.
     * @param s Корабель для якого виконується перевірка.
     * @param shipArr Масив із всіма кораблями.
     * @param position Координати (рядок і стовпець) лівого верхнього кута корабля s.
     * @return true, якщо поставити корабель в задану позицію можна, false, якщо ні.
     */
    default boolean canPlace(Ship s, Ship[] shipArr, GridPosition position) {
        for (int sn = 0; sn < 10; sn++) {
            if (s.getShipID() != sn ) {
                ShipUsedArea usedArea = shipArr[sn].getUsedArea();

                if (s.isVertical()) {
                    for (int i = position.y(); i < position.y() + s.getShipSize(); i++) {
                        if (position.x() >= usedArea.xMin() && position.x() <= usedArea.xMax() && i >= usedArea.yMin() && i <= usedArea.yMax()) {
                            return false;
                        }
                    }
                }
                else {
                    for (int i = position.x(); i < position.x() + s.getShipSize(); i++) {
                        if (i >= usedArea.xMin() && i <= usedArea.xMax() && position.y() >= usedArea.yMin() && position.y() <= usedArea.yMax()) {
                            return false;
                        }
                    }
                }
            }
        }

        return true;
    }

    /**
     * autoplaceShips випадково розташовує кораблі на їх панелі.
     * @param shipArr Масив кораблей які будуть розташовані.
     * @param squareSize Довжина однієї сторони клітини в пікселях.
     * @param fieldSpots Кількість рядків та стовспців на панелі.
     */
    default void autoplaceShips(Ship[] shipArr, int squareSize, int fieldSpots) {
        int gridx, gridy;
        boolean shipNotPlaced, randVertical;
        Random rand = new Random();

        for (int i = 0; i < 10; i++) {
            shipNotPlaced = true;
            while (shipNotPlaced) {
                gridx = rand.nextInt(fieldSpots - 1);
                gridy = rand.nextInt(fieldSpots - 1);
                randVertical = rand.nextBoolean();

                if (shipArr[i].isVertical() != randVertical) {
                    shipArr[i].flipIsVertical();
                }

                if (shipArr[i].isVertical()) {
                    shipArr[i].setX(gridx * squareSize - shipArr[i].getOffset());
                    shipArr[i].setY(gridy * squareSize + shipArr[i].getOffset());
                }
                else {
                    shipArr[i].setX(gridx * squareSize);
                    shipArr[i].setY(gridy * squareSize);
                }

                if(canPlace(shipArr[i], shipArr, getPosition(shipArr[i], squareSize))) {
                    GridPosition shipPos = getPosition(shipArr[i], squareSize);
                    if (shipArr[i].isVertical()) {
                        shipArr[i].setX(shipPos.x() * squareSize - shipArr[i].getOffset());
                        shipArr[i].setY(shipPos.y() * squareSize + shipArr[i].getOffset());
                    }
                    else {
                        shipArr[i].setX(shipPos.x() * squareSize);
                        shipArr[i].setY(shipPos.y() * squareSize);
                    }

                    shipArr[i].draw();
                    shipNotPlaced = false;
                }
            }
        }
    }

    /**
     * drawShips викликає метод draw() для кожного елемента масиву кораблей.
     * @param shipArr Масив кораблей, для яких буде викликан метод draw().
     */
    default void drawShips(Ship[] shipArr) {
        for (int i = 0; i < 10; i++) {
            shipArr[i].draw();
        }
    }
}

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
        if (s == null)
            throw new IllegalArgumentException("s не може бути null в методі getPosition класу GridCalculations.");
        if (squareSize <= 0)
            throw new IllegalArgumentException("squareSize не може бути менше або дорівнювати 0 в методі getPosition класу GridCalculations.");

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
     * createShips приєднує прямокутники кораблів до потрібної панелі та розташовує їх один під одним на певних координатах.
     * @param shipArr Масив кораблей для яких виконується маніпуляції.
     * @param fieldPane Панель, до якої потрібно приєднати прямокутники кораблі.
     * @param squareSize Довжина однієї сторони клітини в пікселях.
     * @param shipStartX Координата X від заданої fieldPane, на якій відбувається розташування.
     * @param shipStartY Координата Y від заданої fieldPane, на якій відбувається розташування.
     */
    default void createShips (Ship[] shipArr, Pane fieldPane, int squareSize, int shipStartX, int shipStartY, boolean makeVisible) {
        if (shipArr == null)
            throw new IllegalArgumentException("shipArr не може бути null в методі createShips класу GridCalculations.");
        if (fieldPane == null)
            throw new IllegalArgumentException("fieldPane не може бути null в методі createShips класу GridCalculations.");
        if (squareSize <= 0)
            throw new IllegalArgumentException("squareSize не може бути менше або дорівнювати 0 в методі createShips класу GridCalculations.");


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
        if (s == null)
            throw new IllegalArgumentException("s не може бути null в методі canPlace класу GridCalculations.");
        if (shipArr == null)
            throw new IllegalArgumentException("shipArr не може бути null в методі canPlace класу GridCalculations.");
        if (position == null)
            throw new IllegalArgumentException("position не може бути null в методі canPlace класу GridCalculations.");

        for (int sn = 0; sn < shipArr.length; sn++) {
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
        if (shipArr == null)
            throw new IllegalArgumentException("shipArr не може бути null в методі autoplaceShips класу GridCalculations.");
        if (squareSize <= 0)
            throw new IllegalArgumentException("squareSize не може бути менше або дорівнювати 0 в методі autoplaceShips класу GridCalculations.");
        if (fieldSpots <= 0)
            throw new IllegalArgumentException("fieldSpots не може бути менше або дорівнювати 0 в методі autoplaceShips класу GridCalculations.");

        int gridx, gridy;
        boolean shipNotPlaced, randVertical;
        Random rand = new Random();

        for (Ship ship : shipArr) {
            shipNotPlaced = true;
            while (shipNotPlaced) {
                gridx = rand.nextInt(fieldSpots - 1);
                gridy = rand.nextInt(fieldSpots - 1);
                randVertical = rand.nextBoolean();

                if (ship.isVertical() != randVertical) {
                    ship.flipIsVertical();
                }

                if (ship.isVertical()) {
                    ship.setX(gridx * squareSize - ship.getOffset());
                    ship.setY(gridy * squareSize + ship.getOffset());
                } else {
                    ship.setX(gridx * squareSize);
                    ship.setY(gridy * squareSize);
                }

                if (canPlace(ship, shipArr, getPosition(ship, squareSize))) {
                    GridPosition shipPos = getPosition(ship, squareSize);
                    if (ship.isVertical()) {
                        ship.setX(shipPos.x() * squareSize - ship.getOffset());
                        ship.setY(shipPos.y() * squareSize + ship.getOffset());
                    } else {
                        ship.setX(shipPos.x() * squareSize);
                        ship.setY(shipPos.y() * squareSize);
                    }

                    ship.draw();
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
        if (shipArr == null)
            throw new IllegalArgumentException("shipArr не може бути null в методі drawShips класу GridCalculations.");

        for (Ship ship : shipArr) {
            ship.draw();
        }
    }
}

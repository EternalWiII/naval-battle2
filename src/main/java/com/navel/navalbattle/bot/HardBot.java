package com.navel.navalbattle.bot;

import com.navel.navalbattle.records.GridPosition;
import com.navel.navalbattle.records.spotStatus;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class HardBot implements Bot {
    List<List<Integer>> coordinatesList = new ArrayList<>();
    Random rand = new Random();
    public HardBot() {
        int index = 0;

        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                coordinatesList.add(new ArrayList<>());

                coordinatesList.get(index).add(i);
                coordinatesList.get(index).add(j);

                index++;
            }
        }
    }
    @Override
    public GridPosition makeDicision(List<List<spotStatus>> isAlreadyHit) {
        List<Integer> actionsList;
        int curAction;

        for (int x = 0; x < 10; x++) {
            for (int y = 0; y < 10; y++) {
                if (isAlreadyHit.get(x).get(y) == spotStatus.HIT) {

                    actionsList = new ArrayList<>() {//!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
                        {
                            add(0);
                            add(1);
                            add(2);
                            add(3);
                        }
                    };

                    while(!actionsList.isEmpty()) {
                        curAction = rand.nextInt(actionsList.size());

                        switch (actionsList.get(curAction)) {
                            case 0:
                                if (x > 0 && isAlreadyHit.get(x - 1).get(y) == spotStatus.HIT) {
                                    if (y - 1 >= 0) {
                                        isAlreadyHit.get(x - 1).set(y - 1, spotStatus.EMPTY);
                                        isAlreadyHit.get(x).set(y - 1, spotStatus.EMPTY);
                                    }

                                    if (y + 1 <= 9) {
                                        isAlreadyHit.get(x - 1).set(y + 1, spotStatus.EMPTY);
                                        isAlreadyHit.get(x).set(y + 1, spotStatus.EMPTY);
                                    }

                                    if (x + 1 <= 9 && isAlreadyHit.get(x + 1).get(y) == spotStatus.UNKNOWN) {
                                        coordinatesList.remove(new ArrayList<>(List.of(x + 1, y)));
                                        return new GridPosition(x + 1, y);
                                    }
                                }
                                actionsList.remove(curAction);
                                break;
                            case 1:
                                if (x < 9 && isAlreadyHit.get(x + 1).get(y) == spotStatus.HIT) {
                                    if (y - 1 >= 0) {
                                        isAlreadyHit.get(x + 1).set(y - 1, spotStatus.EMPTY);
                                        isAlreadyHit.get(x).set(y - 1, spotStatus.EMPTY);
                                    }

                                    if (y + 1 <= 9) {
                                        isAlreadyHit.get(x + 1).set(y + 1, spotStatus.EMPTY);
                                        isAlreadyHit.get(x).set(y + 1, spotStatus.EMPTY);
                                    }

                                    if (x - 1 >= 0 && isAlreadyHit.get(x - 1).get(y) == spotStatus.UNKNOWN) {
                                        coordinatesList.remove(new ArrayList<>(List.of(x - 1, y)));
                                        return new GridPosition(x - 1, y);
                                    }
                                }
                                actionsList.remove(curAction);
                                break;
                            case 2:
                                if (y > 0 && isAlreadyHit.get(x).get(y - 1) == spotStatus.HIT) {
                                    if (x - 1 >= 0) {
                                        isAlreadyHit.get(x - 1).set(y - 1, spotStatus.EMPTY);
                                        isAlreadyHit.get(x - 1).set(y, spotStatus.EMPTY);
                                    }
                                    if (x + 1 <= 9) {
                                        isAlreadyHit.get(x + 1).set(y - 1, spotStatus.EMPTY);
                                        isAlreadyHit.get(x + 1).set(y, spotStatus.EMPTY);
                                    }

                                    if (y + 1 <= 9 && isAlreadyHit.get(x).get(y + 1) == spotStatus.UNKNOWN) {
                                        coordinatesList.remove(new ArrayList<>(List.of(x, y + 1)));
                                        return new GridPosition(x, y + 1);
                                    }
                                }
                                actionsList.remove(curAction);
                                break;
                            case 3:
                                if (y < 9 && isAlreadyHit.get(x).get(y + 1) == spotStatus.HIT) {
                                    if (x - 1 >= 0) {
                                        isAlreadyHit.get(x - 1).set(y, spotStatus.EMPTY);
                                        isAlreadyHit.get(x - 1).set(y + 1, spotStatus.EMPTY);
                                    }
                                    if ( x + 1 <= 9) {
                                        isAlreadyHit.get(x + 1).set(y + 1, spotStatus.EMPTY);
                                        isAlreadyHit.get(x + 1).set(y, spotStatus.EMPTY);
                                    }

                                    if (y - 1 >= 0 && isAlreadyHit.get(x).get(y - 1) == spotStatus.UNKNOWN) {
                                        coordinatesList.remove(new ArrayList<>(List.of(x, y - 1)));
                                        return new GridPosition(x, y - 1);
                                    }
                                }
                                actionsList.remove(curAction);
                                break;
                        }
                    }

                    actionsList = new ArrayList<>() { //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
                        {
                            add(0);
                            add(1);
                            add(2);
                            add(3);
                        }
                    };

                    while (!actionsList.isEmpty()) {

                        curAction = rand.nextInt(actionsList.size());

                        switch (actionsList.get(curAction)) {
                            case 0:
                                if (x > 0 && isAlreadyHit.get(x - 1).get(y) == spotStatus.UNKNOWN) {
                                    coordinatesList.remove(new ArrayList<>(List.of(x - 1, y)));
                                    actionsList.remove(curAction);
                                    return new GridPosition(x - 1, y);
                                }
                                actionsList.remove(curAction);
                                break;
                            case 1:
                                if (x < 9 && isAlreadyHit.get(x + 1).get(y) == spotStatus.UNKNOWN) {
                                    coordinatesList.remove(new ArrayList<>(List.of(x + 1, y)));
                                    actionsList.remove(curAction);
                                    return new GridPosition(x + 1, y);
                                }
                                actionsList.remove(curAction);
                                break;
                            case 2:
                                if (y > 0 && isAlreadyHit.get(x).get(y - 1) == spotStatus.UNKNOWN) {
                                    coordinatesList.remove(new ArrayList<>(List.of(x, y - 1)));
                                    actionsList.remove(curAction);
                                    return new GridPosition(x, y - 1);
                                }
                                actionsList.remove(curAction);
                                break;
                            case 3:
                                if (y < 9 && isAlreadyHit.get(x).get(y + 1) == spotStatus.UNKNOWN) {
                                    coordinatesList.remove(new ArrayList<>(List.of(x, y + 1)));
                                    actionsList.remove(curAction);
                                    return new GridPosition(x, y + 1);
                                }
                                actionsList.remove(curAction);
                                break;
                        }
                    }
                }
            }
        }

        int index = rand.nextInt(coordinatesList.size());

        int x = coordinatesList.get(index).get(0);
        int y = coordinatesList.get(index).get(1);

        coordinatesList.remove(index);

        return new GridPosition(x, y);
    }
}
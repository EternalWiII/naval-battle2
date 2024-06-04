package com.navel.navalbattle.bot;

import com.navel.navalbattle.records.GridPosition;
import com.navel.navalbattle.records.spotStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class EasyBot implements Bot {
    List<List<Integer>> coordinatesList = new ArrayList<>();
    Random rand = new Random();

    /**
     * Конструктор класу EasyBot.
     */
    public EasyBot() {
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

    /**
     * Робить вибір клітини, по якій потрібно нанести удар.
     * @param isAlreadyHit список списків статусів клітин.
     * @return об'єкт класу GridPosition, який містить координати клітини.
     */
    @Override
    public GridPosition makeDecision(List<List<spotStatus>> isAlreadyHit) {
        if (isAlreadyHit == null)
            throw new IllegalArgumentException("isAlreadyHit не може бути null в методі makeDecision класу EasyBot.");

        int index = rand.nextInt(coordinatesList.size());

        int x = coordinatesList.get(index).get(0);
        int y = coordinatesList.get(index).get(1);

        coordinatesList.remove(index);

        return new GridPosition(x, y);
    }
}

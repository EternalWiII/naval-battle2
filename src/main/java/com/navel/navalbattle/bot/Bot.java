package com.navel.navalbattle.bot;

import com.navel.navalbattle.records.GridPosition;
import com.navel.navalbattle.records.spotStatus;

import java.util.List;

interface Bot {
    /**
     * Виконує удар по кораблям користувача.
     */
    default void makeAction() {

    }

    /**
     * Робить вибір клітини, по якій потрібно нанести удар.
     */
    GridPosition makeDicision(List<List<spotStatus>> isAlreadyHit);
}

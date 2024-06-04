package com.navel.navalbattle.bot;

import com.navel.navalbattle.records.GridPosition;
import com.navel.navalbattle.records.spotStatus;

import java.util.List;

interface Bot {
    /**
     * Робить вибір клітини, по якій потрібно нанести удар.
     */
    GridPosition makeDecision(List<List<spotStatus>> isAlreadyHit);
}

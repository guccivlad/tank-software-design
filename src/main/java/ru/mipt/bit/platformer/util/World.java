package ru.mipt.bit.platformer.util;

import com.badlogic.gdx.math.GridPoint2;

public interface World {
    boolean isInside(GridPoint2 cell);
    boolean isBlocked(GridPoint2 cell);
    boolean canStartStep(TankModel t, Direction dir);
    TankModel findTankAt(GridPoint2 cell);
    void removeTank(TankModel tank);

    default boolean isFree(GridPoint2 cell) {
        return isInside(cell) && !isBlocked(cell);
    }
}

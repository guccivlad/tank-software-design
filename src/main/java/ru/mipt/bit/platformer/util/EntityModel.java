package ru.mipt.bit.platformer.util;

import com.badlogic.gdx.math.GridPoint2;

public abstract class EntityModel {
    protected final GridPoint2 tile;
    protected Direction facing = Direction.RIGHT;

    protected EntityModel(GridPoint2 start) {
        this.tile = new GridPoint2(start);
    }

    public GridPoint2 tile() {
        return new GridPoint2(tile);
    }

    public Direction facing() {
        return facing;
    }
}

package ru.mipt.bit.platformer.util;

import com.badlogic.gdx.math.GridPoint2;

public enum Direction {
    UP(0, 1, 90f),
    LEFT(-1, 0, -180f),
    DOWN(0, -1, -90f),
    RIGHT(1, 0, 0f);

    public final int dx;
    public final int dy;
    public final float rotationDeg;

    Direction(int dx, int dy, float rotationDeg) {
        this.dx = dx;
        this.dy = dy;
        this.rotationDeg = rotationDeg;
    }

    public GridPoint2 addTo(GridPoint2 p) {
        return new GridPoint2(p.x + dx, p.y + dy);
    }
}

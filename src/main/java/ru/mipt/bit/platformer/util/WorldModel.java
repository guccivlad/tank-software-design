package ru.mipt.bit.platformer.util;

import com.badlogic.gdx.math.GridPoint2;

import java.util.HashSet;
import java.util.Set;

import java.util.Objects;

public class WorldModel {
    private final int width;
    private final int height;
    private final Set<GridPoint2> blocked = new HashSet<>();

    public WorldModel(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public int width() {
        return width;
    }

    public int height() {
        return height;
    }

    public void addBlocking(GridPoint2 cell) {
        blocked.add(new GridPoint2(Objects.requireNonNull(cell)));
    }

    public boolean isInside(GridPoint2 cell) {
        return cell.x >= 0 && cell.x < width && cell.y >= 0 && cell.y < height;
    }

    public boolean isBlocked(GridPoint2 cell) {
        return blocked.contains(cell);
    }

    public boolean isFree(GridPoint2 cell) {
        return isInside(cell) && !isBlocked(cell);
    }
}
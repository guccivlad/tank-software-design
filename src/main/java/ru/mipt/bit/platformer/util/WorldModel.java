package ru.mipt.bit.platformer.util;

import com.badlogic.gdx.math.GridPoint2;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import java.util.Objects;

public class WorldModel implements World {
    private final int width;
    private final int height;
    private final Set<GridPoint2> blocked = new HashSet<>();
    private final Set<TankModel> tanks = new HashSet<>();

    public WorldModel(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
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

    public void addTank(TankModel t) {
        tanks.add(t);
    }

    public void removeTank(TankModel t) {
        tanks.remove(t);
    }

    @Override
    public boolean canStartStep(TankModel tank, Direction dir) {
        GridPoint2 from = tank.tile();
        GridPoint2 to = new GridPoint2(from);
        switch (dir) {
            case UP:    to.y += 1; break;
            case DOWN:  to.y -= 1; break;
            case LEFT:  to.x -= 1; break;
            case RIGHT: to.x += 1; break;
        }

        if (!isInside(to) || !isFree(to)) {
            return false;
        }

        for (TankModel other : tanks) {
            if (other == tank) {
                continue;
            }

            if (other.isMoving()) {
                GridPoint2 otherFrom = other.tile();
                GridPoint2 otherTo = other.destination();

                if (otherFrom.equals(from) || otherFrom.equals(to) || otherTo.equals(from) || otherTo.equals(to)) {
                    return false;
                }
            } else {
                if (other.tile().equals(to)) {
                    return false;
                }
            }
        }
        return true;
    }

    public GridPoint2 randomFreeCell(Random random) {
        int x = random.nextInt(width);
        int y = random.nextInt(height);
        GridPoint2 point = new GridPoint2(x, y);

        if (!isInside(point) || !isFree(point)) {
            return null;
        }

        for (TankModel tank : tanks) {
            if (tank.tile().equals(point) || (tank.isMoving() && tank.destination().equals(point))) {
                return null;
            }
        }

        return point;
    }
}
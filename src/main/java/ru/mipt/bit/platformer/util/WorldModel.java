package ru.mipt.bit.platformer.util;

import com.badlogic.gdx.math.GridPoint2;

import java.util.*;

public class WorldModel implements World {
    private final int width;
    private final int height;
    private final Set<GridPoint2> blocked = new HashSet<>();
    private final Set<TankModel> tanks = new HashSet<>();
    private final Set<BulletModel> bullets = new HashSet<>();
    private final List<WorldListener> listeners = new ArrayList<>();

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

    @Override
    public boolean isInside(GridPoint2 cell) {
        return cell.x >= 0 && cell.x < width && cell.y >= 0 && cell.y < height;
    }

    @Override
    public boolean isBlocked(GridPoint2 cell) {
        return blocked.contains(cell);
    }

    public boolean isFree(GridPoint2 cell) {
        return isInside(cell) && !isBlocked(cell);
    }

    public void addTank(TankModel t) {
        tanks.add(t);
    }

    @Override
    public void removeTank(TankModel tank) {
//        tanks.remove(t);
        if (tank == null) {
            return;
        }
        if (tanks.remove(tank)) {
            for (WorldListener listener : new ArrayList<>(listeners)) {
                listener.onTankRemoved(tank);
            }
        }
    }

    public void addBullet(BulletModel b) {
        bullets.add(b);
        for (WorldListener listener : listeners) {
            listener.onBulletAdded(b);
        }
    }

    public void removeBullet(BulletModel b) {
        if (bullets.remove(b)) {
            for (WorldListener listener : listeners) {
                listener.onBulletRemoved(b);
            }
        }
    }

    public void addListener(WorldListener listener) { listeners.add(listener); }

    public void removeListener(WorldListener listener) { listeners.remove(listener); }

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

    @Override
    public TankModel findTankAt(GridPoint2 cell) {
        for (TankModel tank : tanks) {
            if (!tank.isAlive()) {
                continue;
            }
            if (tank.tile().equals(cell)) {
                return tank;
            }
            if (tank.isMoving() && tank.destination().equals(cell)) {
                return tank;
            }
        }
        return null;
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

    public void live(float tickSeconds) {
        for (BulletModel bullet : new ArrayList<>(bullets)) {
            bullet.live(this, tickSeconds);
        }
    }
}
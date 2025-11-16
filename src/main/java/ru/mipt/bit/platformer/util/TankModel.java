package ru.mipt.bit.platformer.util;

import com.badlogic.gdx.math.GridPoint2;

public class TankModel extends EntityModel {
    private final GridPoint2 destination = new GridPoint2();
    private boolean moving = false;
    private int maxHealth = 100;
    private int health = 100;

    public TankModel(GridPoint2 start) {
        super(start);
        this.destination.set(start);
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public int getHealth() {
        return health;
    }

    public boolean tryStartStep(Direction dir, World world) {
        if (moving) {
            return false;
        }
        GridPoint2 next = dir.addTo(tile);
        if (!world.isFree(next)) {
            return false;
        }

        destination.set(next);
        facing = dir;
        moving = true;

        return true;
    }

    public void confirmArrival() {
        if (!moving) {
            return;
        }

        tile.set(destination);
        moving = false;
    }

    public boolean isMoving() {
        return moving;
    }

    public GridPoint2 destination() {
        return new GridPoint2(destination);
    }
}
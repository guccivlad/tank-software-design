package ru.mipt.bit.platformer.util;

import com.badlogic.gdx.math.GridPoint2;

public class TankModel extends EntityModel {
    private final GridPoint2 destination = new GridPoint2();
    private boolean moving = false;
    private int maxHealth = 100;
    private int health = 100;
    private boolean alive = true;

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

    public boolean isAlive() { return alive; }

    public void applyDamage(int dmg, WorldModel world) {
        health -= dmg;
        if (health <= 0) {
            health = 0;
            alive = false;
            world.removeTank(this);
        }
    }

    public boolean tryStartStep(Direction direction, World world) {
        if (moving) {
            return false;
        }

        GridPoint2 target = direction.addTo(tile);
        this.facing = direction;
        if (!world.isInside(target) || !world.isFree(target)) {
            return false;
        }
        this.destination.set(target);
        this.moving = true;

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

    public boolean tryShoot(World world) {
        GridPoint2 muzzle = facing.addTo(tile);
        if (!world.isInside(muzzle)) {
            return false;
        }

        if (world instanceof WorldModel) {
            WorldModel model = (WorldModel) world;
            TankModel target = model.findTankAt(muzzle);
            if (target != null && target != this) {
                target.applyDamage(50, model);
                return true;
            }

            if (model.isBlocked(muzzle)) {
                return true;
            }

            BulletModel bullet = new BulletModel(muzzle, facing, 50);
            model.addBullet(bullet);
            return true;
        }

        return false;
    }

    public void live(World world, float tickSeconds) {
        if (!moving) {
            return;
        }

        if (!world.isFree(destination)) {
            moving = false;
            return;
        }
        tile.set(destination);
        moving = false;
    }
}
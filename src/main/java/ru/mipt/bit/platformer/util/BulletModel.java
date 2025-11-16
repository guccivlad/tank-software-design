package ru.mipt.bit.platformer.util;

import com.badlogic.gdx.math.GridPoint2;

public class BulletModel extends EntityModel {
    private final Direction direction;
    private final int damage;
    private boolean alive = true;

    public BulletModel(GridPoint2 start, Direction direction, int damage) {
        super(start);

        this.direction = direction;
        this.damage = damage;
        this.facing = direction;
    }

    public boolean isAlive() {
        return alive;
    }

    public void destroy(WorldModel world) {
        if (!alive) return;
        alive = false;
        world.removeBullet(this);
    }

    public void live(WorldModel world, float tickSeconds) {
        if (!alive) {
            return;
        }

        GridPoint2 next = direction.addTo(tile);
        if (!world.isInside(next) || world.isBlocked(next)) {
            destroy(world);
            return;
        }

        TankModel target = world.findTankAt(next);
        if (target != null) {
            target.applyDamage(damage, world);
            destroy(world);
            return;
        }

        tile.set(next);
    }
}

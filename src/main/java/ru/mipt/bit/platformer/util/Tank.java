package ru.mipt.bit.platformer.util;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.GridPoint2;

import static com.badlogic.gdx.math.MathUtils.isEqual;
import static ru.mipt.bit.platformer.util.GdxGameUtils.continueProgress;

public class Tank extends Entity {
    private final TileMovement movement;
    private final float speedSeconds;
    private GridPoint2 destination;
    private float moveProgress = 1f;

    public Tank(TextureRegion sprite, GridPoint2 start, TileMovement movement, float speedSeconds) {
        super(sprite, start);
        this.movement = movement;
        this.speedSeconds = speedSeconds;
        this.destination = new GridPoint2(start);
        movement.moveRectangleBetweenTileCenters(bounds, start, start, 1f);
    }

    public boolean isIdle() {
        return isEqual(moveProgress, 1f);
    }

    public void tryGo(Direction dir, GameWorld world) {
        if (!isIdle()) {
            return;
        }
        GridPoint2 next = dir.addTo(tile);
        if (world.isBlocked(next)) {
            return;
        }
        destination.set(next);
        facing = dir;
        moveProgress = 0f;
    }

    public void update(float dt) {
        movement.moveRectangleBetweenTileCenters(bounds, tile, destination, moveProgress);
        moveProgress = continueProgress(moveProgress, dt, speedSeconds);
        if (isIdle()) {
            tile.set(destination);
        }
    }

    public float rotationDeg() {
        return facing.rotationDeg;
    }
}
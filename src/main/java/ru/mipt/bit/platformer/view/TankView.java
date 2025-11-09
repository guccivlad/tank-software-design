package ru.mipt.bit.platformer.view;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import ru.mipt.bit.platformer.util.TankModel;
import ru.mipt.bit.platformer.util.TileMovement;

import static ru.mipt.bit.platformer.util.GdxGameUtils.createBoundingRectangle;
import static ru.mipt.bit.platformer.util.GdxGameUtils.drawTextureRegionUnscaled;

public class TankView {
    private final TankModel model;
    private final TextureRegion region;
    private final Rectangle bounds;
    private final TileMovement movement;
    private float progress = 1f;

    public TankView(TankModel model, TextureRegion region, TileMovement movement) {
        this.model = model;
        this.region = region;
        this.movement = movement;
        this.bounds = createBoundingRectangle(region);
        movement.moveRectangleBetweenTileCenters(bounds, model.tile(), model.tile(), 1f);
    }

    public Rectangle getRenderBounds() {
        return new Rectangle(bounds);
    }

    public void update(float deltaProgress) {
        if (model.isMoving()) {
            progress = Math.min(1f, progress + deltaProgress);
            movement.moveRectangleBetweenTileCenters(bounds, model.tile(), model.destination(), progress);
            if (progress >= 1f) {
                model.confirmArrival();
                progress = 1f;
            }
        } else {
            movement.moveRectangleBetweenTileCenters(bounds, model.tile(), model.tile(), 1f);
        }
    }

    public void render(Batch batch) {
        drawTextureRegionUnscaled(batch, region, bounds, model.facing().rotationDeg);
    }

    public void startAnimation() {
        progress = 0f;
    }
}

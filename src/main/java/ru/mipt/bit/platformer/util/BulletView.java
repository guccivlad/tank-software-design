package ru.mipt.bit.platformer.util;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Rectangle;

import static ru.mipt.bit.platformer.util.GdxGameUtils.*;

public class BulletView {
    private final BulletModel model;
    private final TextureRegion region;
    private final Rectangle bounds;

    public BulletView(TextureRegion region, TiledMapTileLayer layer, BulletModel model) {
        this.model = model;
        this.region = region;
        this.bounds = createBoundingRectangle(region);
        moveRectangleAtTileCenter(layer, bounds, model.tile());
    }

    public void sync(TiledMapTileLayer layer) {
        moveRectangleAtTileCenter(layer, bounds, model.tile());
    }

    public void render(Batch batch) {
        drawTextureRegionUnscaled(batch, region, bounds, model.facing().rotationDeg);
    }

    public BulletModel model() { return model; }
}

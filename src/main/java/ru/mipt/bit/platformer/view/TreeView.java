package ru.mipt.bit.platformer.view;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;

import static ru.mipt.bit.platformer.util.GdxGameUtils.createBoundingRectangle;
import static ru.mipt.bit.platformer.util.GdxGameUtils.drawTextureRegionUnscaled;
import static ru.mipt.bit.platformer.util.GdxGameUtils.moveRectangleAtTileCenter;

public class TreeView {
    private final TextureRegion region;
    private final Rectangle bounds;

    public TreeView(TextureRegion region, TiledMapTileLayer layer, com.badlogic.gdx.math.GridPoint2 tile) {
        this.region = region;
        this.bounds = createBoundingRectangle(region);
        moveRectangleAtTileCenter(layer, bounds, tile);
    }

    public void render(Batch batch) {
        drawTextureRegionUnscaled(batch, region, bounds, 0f);
    }
}

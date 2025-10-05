package ru.mipt.bit.platformer.util;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Rectangle;

import static ru.mipt.bit.platformer.util.GdxGameUtils.createBoundingRectangle;
import static ru.mipt.bit.platformer.util.GdxGameUtils.drawTextureRegionUnscaled;

public class Entity {
    protected final TextureRegion sprite;
    protected final Rectangle bounds;
    protected GridPoint2 tile;
    protected Direction facing = Direction.RIGHT;

    protected Entity(TextureRegion sprite, GridPoint2 tile) {
        this.sprite = sprite;
        this.tile = new GridPoint2(tile);
        this.bounds = createBoundingRectangle(sprite);
    }

    public GridPoint2 tile() {
        return tile;
    }

    public void render(Batch batch, float rotation) {
        drawTextureRegionUnscaled(batch, sprite, bounds, rotation);
    }

    public Rectangle bounds() {
        return bounds;
    }
}

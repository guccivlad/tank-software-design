package ru.mipt.bit.platformer.util;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;

import static ru.mipt.bit.platformer.util.GdxGameUtils.moveRectangleAtTileCenter;

public class Tree extends Entity {
    public Tree(TextureRegion sprite, GridPoint2 tile, TiledMapTileLayer layer) {
        super(sprite, tile);
        moveRectangleAtTileCenter(layer, bounds, tile);
    }
}
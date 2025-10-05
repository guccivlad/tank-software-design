package ru.mipt.bit.platformer.util;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.maps.MapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Interpolation;
import ru.mipt.bit.platformer.util.TileMovement;

import java.util.HashSet;
import java.util.Set;

import static ru.mipt.bit.platformer.util.GdxGameUtils.createSingleLayerMapRenderer;
import static ru.mipt.bit.platformer.util.GdxGameUtils.getSingleLayer;

public class GameWorld {
    private final TiledMap map;
    private final MapRenderer renderer;
    private final TiledMapTileLayer ground;
    private final TileMovement movement;

    private final Set<GridPoint2> blocked = new HashSet<>();

    public GameWorld(TiledMap map, Batch batch) {
        this.map = map;
        this.renderer = createSingleLayerMapRenderer(map, batch);
        this.ground = getSingleLayer(map);
        this.movement = new TileMovement(ground, Interpolation.smooth);
    }

    public void renderMap() { renderer.render(); }

    public TileMovement movement() { return movement; }
    public TiledMapTileLayer ground() { return ground; }

    public void addBlocking(GridPoint2 cell) { blocked.add(new GridPoint2(cell)); }

    public boolean isBlocked(GridPoint2 cell) { return blocked.contains(cell); }
}
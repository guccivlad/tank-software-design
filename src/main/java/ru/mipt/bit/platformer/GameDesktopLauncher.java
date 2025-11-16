package ru.mipt.bit.platformer;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.MapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Interpolation;
import ru.mipt.bit.platformer.aibot.TankBot;
import ru.mipt.bit.platformer.command.HealthBarCommand;
import ru.mipt.bit.platformer.command.MoveCommand;
import ru.mipt.bit.platformer.command.ShootCommand;
import ru.mipt.bit.platformer.input.GdxKeyQuery;
import ru.mipt.bit.platformer.input.InputHandler;
import ru.mipt.bit.platformer.util.*;
import ru.mipt.bit.platformer.util.TileMovement;
import ru.mipt.bit.platformer.view.TankView;
import ru.mipt.bit.platformer.view.TreeView;

import java.util.*;
import java.util.stream.Collectors;

import static com.badlogic.gdx.Input.Keys.*;
import static com.badlogic.gdx.graphics.GL20.GL_COLOR_BUFFER_BIT;
import static ru.mipt.bit.platformer.util.GdxGameUtils.createSingleLayerMapRenderer;
import static ru.mipt.bit.platformer.util.GdxGameUtils.getSingleLayer;

public class GameDesktopLauncher implements ApplicationListener {
    private static final float MOVE_SPEED_SECONDS = 0.4f;
    private static final int BOTS_COUNT = 1;
    private final Random random = new Random();

    private Batch batch;
    private TiledMap map;
    private MapRenderer mapRenderer;
    private TiledMapTileLayer groundLayer;

    private WorldModel worldModel;
    private TankModel tankModel;
    private List<TreeModel> treeModel;
    private final List<TankBot> bots = new ArrayList<>();
    private final List<TankView> botViews = new ArrayList<>();

    private final Map<BulletModel, BulletView> bulletViews = new HashMap<>();
    private TextureRegion bulletRegion;

    private ShapeRenderer shapes;
    private final List<HealthBarTankViewDecorator> healthViews = new ArrayList<>();

    private final Map<TankModel, TankView> botViewByTank = new HashMap<>();
    private final Map<TankModel, HealthBarTankViewDecorator> hpViewByTank = new HashMap<>();

    private Texture tankTexture, treeTexture;
    private TextureRegion tankRegion, treeRegion;
    private TankView tankView;
    private List<TreeView> treeView;
    private TileMovement tileMovement;

    private InputHandler input;

    @Override
    public void create() {
        batch = new SpriteBatch();
        shapes = new ShapeRenderer();

        map = new TmxMapLoader().load("level.tmx");
        mapRenderer = createSingleLayerMapRenderer(map, batch);
        groundLayer = getSingleLayer(map);
        tileMovement = new TileMovement(groundLayer, Interpolation.smooth);

        worldModel = new WorldModel(groundLayer.getWidth(), groundLayer.getHeight());
        LevelData levelData;
        if (Gdx.files.internal("level.txt").exists()) {
            String fileContent = Gdx.files.internal("level.txt").readString();
            levelData = LevelGenerator.levelFromFile(worldModel, fileContent);
        } else {
            levelData = LevelGenerator.generateRandomLevel(worldModel, 5, new Random());
        }

        tankModel = levelData.tank;
        treeModel = levelData.trees;
        worldModel.addTank(tankModel);

        tankTexture = new Texture("images/tank_blue.png");
        treeTexture = new Texture("images/greenTree.png");
        tankRegion = new TextureRegion(tankTexture);
        treeRegion = new TextureRegion(treeTexture);
        bulletRegion = new TextureRegion(tankTexture, 0, 0, 16, 16);

        tankView = new TankView(tankModel, tankRegion, tileMovement);
        HealthBarTankViewDecorator playerHp = new HealthBarTankViewDecorator(tankView, tankModel);
        healthViews.add(playerHp);
        hpViewByTank.put(tankModel, playerHp);
        for (int i = 0; i < BOTS_COUNT; i++) {
            GridPoint2 spawn = worldModel.randomFreeCell(random);
            if(spawn == null) {
                continue;
            }
            TankModel botTank = new TankModel(spawn);

            worldModel.addTank(botTank);
            bots.add(new TankBot(botTank));
            TankView botView = new TankView(botTank, tankRegion, tileMovement);
            botViews.add(botView);
            healthViews.add(new HealthBarTankViewDecorator(botView, botTank));
        }
        treeView = new ArrayList<>();
        for (TreeModel m : treeModel) {
            treeView.add(new TreeView(treeRegion, groundLayer, m.tile()));
        }

        input = new InputHandler(new GdxKeyQuery())
                .map(Direction.UP, UP, W)
                .map(Direction.LEFT, LEFT, A)
                .map(Direction.DOWN, DOWN, S)
                .map(Direction.RIGHT, RIGHT, D)
                .priority(Direction.UP, Direction.LEFT, Direction.DOWN, Direction.RIGHT);

        worldModel.addListener(new WorldListener() {
            @Override
            public void onBulletAdded(BulletModel bullet) {
                BulletView view = new BulletView(bulletRegion, groundLayer, bullet);
                bulletViews.put(bullet, view);
            }

            @Override
            public void onBulletRemoved(BulletModel bullet) {
                bulletViews.remove(bullet);
            }

            @Override
            public void onTankRemoved(TankModel tank) {
                HealthBarTankViewDecorator hp = hpViewByTank.remove(tank);
                if (hp != null) {
                    healthViews.remove(hp);
                }
                TankView botView = botViewByTank.remove(tank);
                if (botView != null) {
                    botViews.remove(botView);
                }
            }
        });
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0f, 0f, 0.2f, 1f);
        Gdx.gl.glClear(GL_COLOR_BUFFER_BIT);

        float dt = Gdx.graphics.getDeltaTime();

        if (Gdx.input.isKeyJustPressed(L)) {
            new HealthBarCommand().execute(worldModel);
        }

        if (!tankModel.isMoving()) {
            Optional<Direction> dir = input.pollDirection();
            dir.ifPresent(d -> {
                boolean started = new MoveCommand(tankModel, d).execute(worldModel);
                if (started) {
                    tankView.startAnimation();
                }
            });
        }

        if (Gdx.input.isKeyJustPressed(SPACE)) {
            new ShootCommand(tankModel).execute(worldModel);
        }

        for (TankBot bot : bots) {
            bot.update(worldModel, dt);
        }

        worldModel.live(dt);

        tankView.update(dt / MOVE_SPEED_SECONDS);
        for (TankView botView : botViews) {
            botView.update(dt / MOVE_SPEED_SECONDS);
        }


        mapRenderer.render();
        batch.begin();
        for (TreeView tv : treeView) {
            tv.render(batch);
        }
        for (TankView botView : botViews) {
            botView.render(batch);
        }
        tankView.render(batch);
        for (BulletView bulletView : bulletViews.values()) {
            bulletView.sync(groundLayer);
            bulletView.render(batch);
        }
        batch.end();

        for (HealthBarTankViewDecorator healthView : healthViews) {
            healthView.renderHp(shapes);
        }
    }

    @Override
    public void resize(int width, int height) { }

    @Override
    public void pause() { }

    @Override
    public void resume() { }

    @Override
    public void dispose() {
        tankTexture.dispose();
        treeTexture.dispose();
        map.dispose();
        batch.dispose();
        shapes.dispose();
    }

    public static void main(String[] args) {
        Lwjgl3ApplicationConfiguration cfg = new Lwjgl3ApplicationConfiguration();
        cfg.setWindowedMode(1280, 1024);
        new Lwjgl3Application(new GameDesktopLauncher(), cfg);
    }
}
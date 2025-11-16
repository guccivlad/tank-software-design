package ru.mipt.bit.platformer.util;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import ru.mipt.bit.platformer.command.HealthBarCommand;
import ru.mipt.bit.platformer.view.TankView;

public class HealthBarTankViewDecorator {
    private static final float BAR_WIDTH = 25f;
    private static final float BAR_HEIGHT = 5f;
    private static final float BAR_OFFSET_Y = 20f;

    private final TankView view;
    private final TankModel model;

    public HealthBarTankViewDecorator(TankView view, TankModel model) {
        this.view = view;
        this.model = model;
    }

    public void renderHp(ShapeRenderer shapes) {
        if (!HealthBarCommand.isHealthBarsVisible()) {
            return;
        }

        Rectangle rb = view.getRenderBounds();
        float barX = rb.x + (rb.width - BAR_WIDTH) / 2f;
        float barY = rb.y + rb.height + BAR_OFFSET_Y;

        int maxHealth = model.getMaxHealth();
        float fraction = Math.max(0f, Math.min(1f, (float) model.getHealth() / (float) maxHealth));

        shapes.begin(ShapeRenderer.ShapeType.Filled);
        shapes.setColor(0f, 1f, 0f, 1f);
        shapes.rect(barX, barY, BAR_WIDTH * fraction, BAR_HEIGHT);
        shapes.end();
    }
}

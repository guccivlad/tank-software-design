package ru.mipt.bit.platformer.aibot;

import ru.mipt.bit.platformer.command.MoveCommand;
import ru.mipt.bit.platformer.util.Direction;
import ru.mipt.bit.platformer.util.TankModel;
import ru.mipt.bit.platformer.util.World;

import java.util.Random;

public class TankBot {
    private final TankModel tank;
    private final Random random = new Random();
    private float movePeriodSec = 1.5f;
    private float timer = 0f;

    public TankBot(TankModel tank) {
        this.tank = tank;
    }

    public void update(World world, float dt) {
        timer += dt;
        if (timer < movePeriodSec) {
            return;
        }

        if (tank.isMoving()) {
            return;
        }

        Direction[] directions = Direction.values();
        Direction direction = directions[random.nextInt(directions.length)];
        boolean started = new MoveCommand(tank, direction).execute(world);
        if (started) {
            timer = 0f;
        } else {
            timer = movePeriodSec;
        }
    }
}
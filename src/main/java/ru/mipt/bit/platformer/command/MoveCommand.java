package ru.mipt.bit.platformer.command;

import ru.mipt.bit.platformer.util.Direction;
import ru.mipt.bit.platformer.util.TankModel;
import ru.mipt.bit.platformer.util.World;

public class MoveCommand implements Command {
    private final TankModel tank;
    private final Direction direction;

    public MoveCommand(TankModel tank, Direction direction) {
        this.tank = tank;
        this.direction = direction;
    }

    @Override
    public boolean execute(World world) {
        if (tank.isMoving()) {
            return false;
        }

        if (!world.canStartStep(tank, direction)) {
            return false;
        }

        return tank.tryStartStep(direction, world);
    }
}
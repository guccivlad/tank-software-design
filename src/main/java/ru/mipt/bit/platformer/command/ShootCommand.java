package ru.mipt.bit.platformer.command;

import ru.mipt.bit.platformer.util.TankModel;
import ru.mipt.bit.platformer.util.World;

public class ShootCommand implements Command {
    private final TankModel tank;

    public ShootCommand(TankModel tank) {
        this.tank = tank;
    }

    @Override
    public boolean execute(World world) {
        return tank.tryShoot(world);
    }
}

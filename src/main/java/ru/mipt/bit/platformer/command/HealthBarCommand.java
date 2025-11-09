package ru.mipt.bit.platformer.command;

import ru.mipt.bit.platformer.util.World;

public class HealthBarCommand implements Command {
    private static boolean healthBarVisible = false;

    public static boolean isHealthBarsVisible() {
        return healthBarVisible;
    }

    public static void toggle() {
        healthBarVisible = !healthBarVisible;
    }

    @Override
    public boolean execute(World world) {
        toggle();
        return true;
    }
}

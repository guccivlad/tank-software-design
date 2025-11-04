package ru.mipt.bit.platformer.command;

import ru.mipt.bit.platformer.util.World;

public interface Command {
    boolean execute(World world);
}
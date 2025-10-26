package ru.mipt.bit.platformer.util;

import java.util.List;

public class LevelData {
    public final TankModel tank;
    public final List<TreeModel> trees;

    public LevelData(TankModel tank, List<TreeModel> trees) {
        this.tank = tank;
        this.trees = trees;
    }
}

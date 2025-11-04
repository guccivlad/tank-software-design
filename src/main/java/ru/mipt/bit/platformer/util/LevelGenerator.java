package ru.mipt.bit.platformer.util;

import com.badlogic.gdx.math.GridPoint2;

import java.util.*;

public final class LevelGenerator {
    private LevelGenerator() {}

    private static GridPoint2 randomCell(WorldModel world, Random random, Set<GridPoint2> occupied) {
        int w = world.getWidth();
        int h = world.getHeight();

        for (int attempts = 0; attempts < w * h; attempts++) {
            GridPoint2 candidate = new GridPoint2(
                    random.nextInt(w),
                    random.nextInt(h)
            );
            if (occupied.contains(candidate)) {
                continue;
            }
            if (!world.isFree(candidate)) {
                continue;
            }

            return candidate;
        }

        return null;
    }

    public static LevelData generateRandomLevel(WorldModel world, int treeCount, Random random) {
        Set<GridPoint2> occupied = new HashSet<>();
        List<TreeModel> trees = new ArrayList<>();

        GridPoint2 tankPos = randomCell(world, random, occupied);
        occupied.add(tankPos);
        TankModel tank = new TankModel(tankPos);

        for (int i = 0; i < treeCount; i++) {
            GridPoint2 treePos = randomCell(world, random, occupied);
            if (treePos == null) {
                break;
            }
            occupied.add(treePos);

            TreeModel tree = new TreeModel(treePos);
            trees.add(tree);
            world.addBlocking(treePos);
        }

        return new LevelData(tank, trees);
    }

    public static LevelData levelFromFile(WorldModel world, String fileContent) {
        List<TreeModel> trees = new ArrayList<>();
        TankModel tank = null;

        String[] linesRaw = fileContent.split("\\R");
        List<String> lines = new ArrayList<>();
        for (String line : linesRaw) {
            if (!line.isEmpty() || !lines.isEmpty()) {
                lines.add(line);
            }
        }

        int fileHeight = lines.size();
        for (int rowFromTop = 0; rowFromTop < fileHeight; rowFromTop++) {
            String line = lines.get(rowFromTop);
            int fileWidth = line.length();
            int y = fileHeight - 1 - rowFromTop;

            for (int x = 0; x < fileWidth; x++) {
                char ch = line.charAt(x);

                GridPoint2 cell = new GridPoint2(x, y);

                switch (ch) {
                    case 'T': {
                        TreeModel tree = new TreeModel(cell);
                        trees.add(tree);
                        world.addBlocking(cell);
                        break;
                    }
                    case 'X': {
                        if (tank == null) {
                            tank = new TankModel(cell);
                        }
                        break;
                    }
                    default:
                        break;
                }
            }
        }

        return new LevelData(tank, trees);
    }
}

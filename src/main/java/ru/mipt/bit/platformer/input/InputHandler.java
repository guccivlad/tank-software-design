package ru.mipt.bit.platformer.input;

import ru.mipt.bit.platformer.util.Direction;

import java.util.EnumMap;
import java.util.Map;
import java.util.Optional;

public class InputHandler {
    private final Map<Direction, int[]> mapping = new EnumMap<>(Direction.class);
    private final KeyQuery keyQuery;
    private Direction[] priority = {
            Direction.UP,
            Direction.LEFT,
            Direction.DOWN,
            Direction.RIGHT
    };

    public InputHandler(KeyQuery keyQuery) {
        this.keyQuery = keyQuery;
    }

    public InputHandler map(Direction dir, int... keys) {
        mapping.put(dir, keys);

        return this;
    }

    public InputHandler priority(Direction... order) {
        this.priority = order.clone();

        return this;
    }

    public Optional<Direction> pollDirection() {
        for (Direction dir : priority) {
            int[] keys = mapping.get(dir);

            if (keys == null) {
                continue;
            }

            for (int k : keys) {
                if (keyQuery.isPressed(k)) {
                    return Optional.of(dir);
                }
            }
        }

        return Optional.empty();
    }
}

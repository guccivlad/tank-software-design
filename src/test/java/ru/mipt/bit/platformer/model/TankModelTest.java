package ru.mipt.bit.platformer.model;

import com.badlogic.gdx.math.GridPoint2;
import org.junit.jupiter.api.Test;
import ru.mipt.bit.platformer.util.Direction;
import ru.mipt.bit.platformer.util.TankModel;
import ru.mipt.bit.platformer.util.WorldModel;

import static org.junit.jupiter.api.Assertions.*;

class TankModelTest {
    @Test
    void startsStepOnlyIfFreeAndIdle() {
        WorldModel world = new WorldModel(10, 8);
        TankModel tank = new TankModel(new GridPoint2(1,1));

        // поехали
        assertTrue(tank.tryStartStep(Direction.UP, world));
        assertTrue(tank.isMoving());
        assertEquals(Direction.UP, tank.facing());
        assertEquals(new GridPoint2(1,2), tank.destination());

        // доехали
        tank.confirmArrival();

        assertFalse(tank.isMoving());
        assertEquals(new GridPoint2(1,2), tank.tile());
    }

    @Test
    void cannotLeaveBounds() {
        WorldModel world = new WorldModel(2, 2);
        TankModel tank = new TankModel(new GridPoint2(0,0));

        assertFalse(tank.tryStartStep(Direction.LEFT, world));
        assertFalse(tank.tryStartStep(Direction.DOWN, world));
        assertTrue(tank.tryStartStep(Direction.RIGHT, world));
    }
}

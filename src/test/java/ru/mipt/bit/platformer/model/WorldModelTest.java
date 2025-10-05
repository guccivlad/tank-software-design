package ru.mipt.bit.platformer.model;

import com.badlogic.gdx.math.GridPoint2;
import org.junit.jupiter.api.Test;
import ru.mipt.bit.platformer.util.WorldModel;

import static org.junit.jupiter.api.Assertions.*;

class WorldModelTest {
    @Test
    void boundsAndBlocking() {
        WorldModel w = new WorldModel(10, 8);

        assertTrue(w.isInside(new GridPoint2(0,0)));
        assertTrue(w.isInside(new GridPoint2(2,7)));
        assertFalse(w.isInside(new GridPoint2(-1,0)));
        assertFalse(w.isInside(new GridPoint2(10,0)));

        GridPoint2 t = new GridPoint2(4,5);

        assertTrue(w.isFree(t));

        w.addBlocking(t);

        assertTrue(w.isBlocked(t));
        assertFalse(w.isFree(t));
    }
}
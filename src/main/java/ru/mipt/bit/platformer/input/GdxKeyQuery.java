package ru.mipt.bit.platformer.input;

import com.badlogic.gdx.Gdx;

public class GdxKeyQuery implements KeyQuery {
    @Override
    public boolean isPressed(int keyCode) {
        return Gdx.input.isKeyPressed(keyCode);
    }
}

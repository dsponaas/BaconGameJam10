package com.grizbenzis.bgj10;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;

/**
 * Created by sponaas on 1/22/16.
 */
public class InputManager implements InputProcessor {

    public static boolean shootingActive = false;
    public static boolean rotateLeftActive = false;
    public static boolean rotateRightActive = false;

    private static final int SHOOTING_KEY = Input.Keys.SPACE;
    private static final int ROTATE_LEFT_KEY = Input.Keys.LEFT;
    private static final int ROTATE_RIGHT_KEY = Input.Keys.RIGHT;

    @Override
    public boolean keyDown(int keycode)
    {
        switch (keycode)
        {
            case SHOOTING_KEY:
                shootingActive = true;
                break;
            case ROTATE_LEFT_KEY:
                rotateLeftActive = true;
                break;
            case ROTATE_RIGHT_KEY:
                rotateRightActive = true;
                break;
        }
        return true;
    }

    @Override
    public boolean keyUp(int keycode)
    {
        switch (keycode)
        {
            case SHOOTING_KEY:
                shootingActive = false;
                break;
            case ROTATE_LEFT_KEY:
                rotateLeftActive = false;
                break;
            case ROTATE_RIGHT_KEY:
                rotateRightActive = false;
                break;
        }
        return true;
    }
    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}

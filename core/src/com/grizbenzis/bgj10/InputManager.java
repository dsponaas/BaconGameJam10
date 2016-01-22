package com.grizbenzis.bgj10;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;

/**
 * Created by sponaas on 1/22/16.
 */
public class InputManager implements InputProcessor {

    public static boolean moveUpActive = false;
    public static boolean moveDownActive = false;
    public static boolean moveLeftActive = false;
    public static boolean moveRightActive = false;

    public static boolean shootingUpActive = false;
    public static boolean shootingDownActive = false;
    public static boolean shootingLeftActive = false;
    public static boolean shootingRightActive = false;

    private static final int MOVE_UP_KEY = Input.Keys.W;
    private static final int MOVE_DOWN_KEY = Input.Keys.S;
    private static final int MOVE_LEFT_KEY = Input.Keys.A;
    private static final int MOVE_RIGHT_KEY = Input.Keys.D;

    private static final int SHOOTING_UP_KEY = Input.Keys.UP;
    private static final int SHOOTING_DOWN_KEY = Input.Keys.DOWN;
    private static final int SHOOTING_LEFT_KEY = Input.Keys.LEFT;
    private static final int SHOOTING_RIGHT_KEY = Input.Keys.RIGHT;

    @Override
    public boolean keyDown(int keycode)
    {
        switch (keycode)
        {
            case MOVE_UP_KEY:
                moveUpActive = true;
                break;
            case MOVE_DOWN_KEY:
                moveDownActive = true;
                break;
            case MOVE_LEFT_KEY:
                moveLeftActive = true;
                break;
            case MOVE_RIGHT_KEY:
                moveRightActive = true;
                break;
            case SHOOTING_UP_KEY:
                shootingUpActive = true;
                shootingDownActive = false;
                shootingLeftActive = false;
                shootingRightActive = false;
                break;
            case SHOOTING_DOWN_KEY:
                shootingDownActive = true;
                shootingUpActive = false;
                shootingLeftActive = false;
                shootingRightActive = false;
                break;
            case SHOOTING_LEFT_KEY:
                shootingLeftActive = true;
                shootingUpActive = false;
                shootingDownActive = false;
                shootingRightActive = false;
                break;
            case SHOOTING_RIGHT_KEY:
                shootingRightActive = true;
                shootingUpActive = false;
                shootingDownActive = false;
                shootingLeftActive = false;
                break;
        }
        return true;
    }

    @Override
    public boolean keyUp(int keycode)
    {
        switch (keycode)
        {
            case MOVE_UP_KEY:
                moveUpActive = false;
                break;
            case MOVE_DOWN_KEY:
                moveDownActive = false;
                break;
            case MOVE_LEFT_KEY:
                moveLeftActive = false;
                break;
            case MOVE_RIGHT_KEY:
                moveRightActive = false;
                break;
            case SHOOTING_UP_KEY:
                shootingUpActive = false;
                break;
            case SHOOTING_DOWN_KEY:
                shootingDownActive = false;
                break;
            case SHOOTING_LEFT_KEY:
                shootingLeftActive = false;
                break;
            case SHOOTING_RIGHT_KEY:
                shootingRightActive = false;
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

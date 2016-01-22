package com.grizbenzis.bgj10;

/**
 * Created by sponaas on 1/22/16.
 */
public class Constants {
    public static final float PIXELS_TO_METERS = 0.01f;
    public static final float METERS_TO_PIXELS = 100f;

    public static final int TARGET_FPS = 60;
    public static final String LOG_TAG = "bgj10";

    public static final short BITMASK_PLAYER = 0x0001;
    public static final short BITMASK_ENEMY = 0x0002;
    public static final short BITMASK_LEVEL_BOUNDS = 0x0004;
    public static final short BITMASK_PLAYER_BULLET = 0x0008;
    public static final short BITMASK_POWERUP = 0x0010;

}

package com.grizbenzis.bgj10;

/**
 * Created by sponaas on 1/22/16.
 */
public class Constants {
    public static final float PIXELS_TO_METERS = 0.01f;
    public static final float METERS_TO_PIXELS = 100f;

    public static final int TARGET_FPS = 60;
    public static final String LOG_TAG = "bgj10";

    public static final float TOP_OF_SCREEN_BUFFER = 30f;

    public static final float BASE_SPAWN_TIMER = 120f;
    public static final float TIME_PER_LEVEL = 600f;

    public static final short BITMASK_PLAYER = 0x0001;
    public static final short BITMASK_ENEMY = 0x0002;
    public static final short BITMASK_LEVEL_BOUNDS = 0x0004;
    public static final short BITMASK_PLAYER_BULLET = 0x0008;
    public static final short BITMASK_POWERUP = 0x0010;

    public static final float BULLET_SPEED = .04f;
    public static final float PLAYER_SHOOTING_COOLDOWN = 40f;
    public static final float PLAYER_DEATH_TIME = 180f;
    public static final float PLAYER_INVINCIBILITY_TIME = 180f;

    public static final float TIME_BETWEEN_POWERUPS = 600f;
    public static final float POWERUP_TIMER = 600f;

    public enum PowerupType {
        POINTS_2X,
        RPD_SHOT,
        SPRD_SHOT,
        EXTRA_LIFE;

        public static PowerupType fromInt(int x) {
            switch(x) {
                case 0:
                    return POINTS_2X;
                case 1:
                    return RPD_SHOT;
                case 2:
                    return SPRD_SHOT;
                case 3:
                    return EXTRA_LIFE;
            }
            return null;
        }
    }

}

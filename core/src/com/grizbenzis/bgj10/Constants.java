package com.grizbenzis.bgj10;

/**
 * Created by sponaas on 1/22/16.
 */
public class Constants {
    public static final float TWO_PI = 2f * (float)Math.PI;
    public static final float ONE_AND_A_HALF_PI = 1.5f * (float)Math.PI;
    public static final float PIXELS_TO_METERS = 0.01f;
    public static final float METERS_TO_PIXELS = 100f;

    public static final int TARGET_FPS = 60;
    public static final String LOG_TAG = "bgj10";

    public static final float TOP_OF_SCREEN_BUFFER = 30f;

    public static final float BODY_MAX_SPEED = 5f;

    public static final float BASE_SPAWN_TIMER = 400f;
    public static final float TIME_PER_LEVEL = 600f;
    public static final int INITIAL_LIVES_COUNT = 2;

    public static final short BITMASK_PLAYER = 0x0001;
    public static final short BITMASK_ENEMY = 0x0002;
    public static final short BITMASK_LEVEL_BOUNDS = 0x0004;
    public static final short BITMASK_PLAYER_BULLET = 0x0008;
    public static final short BITMASK_POWERUP = 0x0010;
    public static final short BITMASK_EXPLOSION = 0x0020;
    public static final short BITMASK_BLACK_HOLE = 0x0040;
    public static final short BITMASK_ALIEN = 0x0080;

    public static final float PLAYER_ROTATION_SPEED = 1.5f;
    public static final float BULLET_SPEED = .1f;
    public static final float BULLET_EXPLOSION_DURATION = 30f;
    public static final float SHOOTING_COOLDOWN = 12f;
    public static final float SHOOTING_CHARGE_TIME = 24f;
    public static final float PLAYER_DEATH_TIME = 180f;
    public static final float PLAYER_INVINCIBILITY_TIME = 180f;
    public static final float SPREAD_SHOT_ANGLE = (float)Math.PI * 0.1f;
    public static final float SHOOTING_MIN_POWER_FACTOR = 0.3f;
    public static final float SHOOTING_MAX_POWER_FACTOR = 1.5f;
    public static final float SHIP_ACCEL_FACTOR = 0.8f;

    public static final float BLOOD_FADE_TIME = 20f;
    public static final float BLEED_INTERVAL = 5f;

    public static final float ALIEN_DEFAULT_SPAWN_TIMER = 600f;
    public static final float ALIEN_SPAWN_SPEED_FACTOR = 1.025f;

    public static final float LARGE_ASTEROID_SPAWN_SPEED_FACTOR = 0.5f;
    public static final float LARGE_ASTEROID_TORQUE_FACTOR = 1.5f;
    public static final float MEDIUM_ASTEROID_SPAWN_SPEED_FACTOR = .5f;
    public static final int MEDIUM_ASTEROID_SPAWN_QUANTITY = 2;
    public static final float MEDIUM_ASTEROID_TORQUE_FACTOR = 0.4f;
    public static final float SMALL_ASTEROID_SPAWN_SPEED_FACTOR = .3f;
    public static final int SMALL_ASTEROID_SPAWN_QUANTITY = 2;
    public static final float SMALL_ASTEROID_TORQUE_FACTOR = 0.2f;

    public static final float BLACK_HOLE_DEFAULT_SPAWN_TIMER = 750f;
    public static final int BLACK_HOLE_MIN_LEVEL = 4;
    public static final float BLACK_HOLE_POS_BUFFER = 300f;
    public static final float BLACK_HOLE_MAX_GRAV_DIST_SQUARED = 120000f;
    public static final float BLACK_HOLE_LIFE_TIME = 180f;
    public static final float BLACK_HOLE_MAX_GRAV = 0.2f;

    public static final float PLAYER_SHOOTING_VOLUME = 0.2f;
    public static final float EXPLOSION_VOLUME = 0.6f;
    public static final float DEATH_VOLUME = 1f;

    public static final float TIME_BETWEEN_POWERUPS = 600f;
    public static final float POWERUP_TIMER = 600f;

    public enum PowerupType {
        POINTS_2X,
        EXPLOSION_UP,
        SPRD_SHOT,
        EXTRA_LIFE;

        public static PowerupType fromInt(int x) {
            switch(x) {
                case 0:
                    return POINTS_2X;
                case 1:
                    return EXPLOSION_UP;
                case 2:
                    return SPRD_SHOT;
                case 3:
                    return EXTRA_LIFE;
            }
            return null;
        }
    }

    public enum WeaponState {
        READY,
        CHARGING,
        COOL_DOWN,
    }

    public enum EnemyType {
        ASTEROID_LARGE,
        ASTEROID_MEDIUM,
        ASTEROID_SMALL,
        ALIEN
    }

}

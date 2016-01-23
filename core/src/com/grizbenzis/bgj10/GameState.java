package com.grizbenzis.bgj10;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.grizbenzis.bgj10.actors.Player;
import com.grizbenzis.bgj10.components.*;

import java.util.Random;

/**
 * Created by sponaas on 1/22/16.
 */
public class GameState {

    private static GameState _instance;

    private float _width, _height;
    private float _enemySpawnTimer;
    private float _powerupSpawnTimer;
    private Random _rand;

    private float _levelTimer;
    private int _level;
    public int getLevel()                   { return _level; }

    private int _lives;
    public int getLives()                   { return _lives; }
    public void decrementLives()            { --_lives; }
    public void incrementLives()            { ++_lives; }

    private int _score;
    public int getScore()                   { return _score; }
    public void incrementScore(int val) {
        if(getPlayerData().points2xTime >= 0f)
            val *= 2;
        _score += val;
    }

    public float getWidth()             { return _width; }
    public float getHeight()            { return _height; }

    private Player _player;
    public Player getPlayer()               { return _player; }
    public void setPlayer(Player player)    { _player = player; }
    public PlayerDataComponent getPlayerData()      { return _playerDataComponents.get(_player.getEntity()); }

    private ComponentMapper<PlayerDataComponent> _playerDataComponents = ComponentMapper.getFor(PlayerDataComponent.class);

    private GameState(float width, float height) {
        _width = width;
        _height = height;
        _rand = new Random();

        _level = 1;
        _levelTimer = Constants.TIME_PER_LEVEL;

        _powerupSpawnTimer = Constants.TIME_BETWEEN_POWERUPS;

        _lives = 2;

        _enemySpawnTimer = getSpawnTimer();
    }

    public static void initialize(float width, float height) {
        // TODO: we're not going to have anything to dispose, yes?
        _instance = new GameState(width, height);
    }

    public static GameState getInstance() {
        return _instance;
    }

    public void update() {
        PlayerDataComponent playerData = getPlayerData();

        if(playerData.alive && (playerData.invincibilityTime <= 0f)) {
            _enemySpawnTimer -= (float) Time.time;
            _levelTimer -= (float) Time.time;
            _powerupSpawnTimer -= (float) Time.time;
        }

        if(_enemySpawnTimer < 0f) {
            _enemySpawnTimer = getSpawnTimer();
            spawnEnemy();
        }
        if(_levelTimer < 0f) {
            ++_level;
            _levelTimer = Constants.TIME_PER_LEVEL;
        }
        if(_powerupSpawnTimer < 0f) {
            _powerupSpawnTimer = Constants.TIME_BETWEEN_POWERUPS;
            spawnPowerup();
        }
    }

    private final float MIN_SPAWN_INCREASE_RATE_HACK = 0.7f;
    private final float MAX_SPAWN_INCREASE_RATE_HACK = 0.9f;
    private final float SPAWN_INCREASE_HACK_MOD = 0.03f;
    private float getSpawnTimer() {
        float spawnTimer = Constants.BASE_SPAWN_TIMER;
        for(int i = 1; i < _level; ++i) {
            float spawnMod = MIN_SPAWN_INCREASE_RATE_HACK + ((float)(i / 2) * SPAWN_INCREASE_HACK_MOD);
            if(spawnMod > MAX_SPAWN_INCREASE_RATE_HACK)
                spawnMod = MAX_SPAWN_INCREASE_RATE_HACK;
            spawnTimer *= spawnMod;
        }
        return spawnTimer;
    }

    private final float POWERUP_LEVEL_BOUNDS_BUFFER = 100f;
    public void spawnPowerup() {
        Entity entity = new Entity();

        int type = _rand.nextInt(Constants.PowerupType.values().length);

        float xPos = getRandomFloat(0f + POWERUP_LEVEL_BOUNDS_BUFFER, _width - POWERUP_LEVEL_BOUNDS_BUFFER);
        float yPos = getRandomFloat(0f + POWERUP_LEVEL_BOUNDS_BUFFER, _height - Constants.TOP_OF_SCREEN_BUFFER - POWERUP_LEVEL_BOUNDS_BUFFER);
        PositionComponent positionComponent = new PositionComponent(xPos, yPos);

        Sprite sprite = null;
        if(type == Constants.PowerupType.RPD_SHOT.ordinal())
            sprite = new Sprite(ResourceManager.getTexture("powerup_rpd"));
        else if(type == Constants.PowerupType.SPRD_SHOT.ordinal())
            sprite = new Sprite(ResourceManager.getTexture("powerup_sprd"));
        else if(type == Constants.PowerupType.POINTS_2X.ordinal())
            sprite = new Sprite(ResourceManager.getTexture("powerup_2x"));
        else if(type == Constants.PowerupType.EXTRA_LIFE.ordinal())
            sprite = new Sprite(ResourceManager.getTexture("powerup_1up"));

        SpriteComponent spriteComponent = new SpriteComponent(sprite);
        BodyComponent bodyComponent = new BodyComponent(positionComponent, BodyFactory.getInstance().generate(entity, "powerup.json", new Vector2(xPos, yPos)));
        PowerupComponent powerupComponent = new PowerupComponent(Constants.PowerupType.fromInt(type));
        RenderComponent renderComponent = new RenderComponent(0);

        entity.add(positionComponent).add(spriteComponent).add(bodyComponent).add(powerupComponent).add(renderComponent);

        EntityManager.getInstance().addEntity(entity);
    }

    private final float ENEMY_LEVEL_BOUNDS_BUFFER = 100f;
    public void spawnEnemy() {
        Entity entity = new Entity();

        // TODO: add more types

        float xPos = getRandomFloat(0f + ENEMY_LEVEL_BOUNDS_BUFFER, _width - ENEMY_LEVEL_BOUNDS_BUFFER);
        float yPos = getRandomFloat(0f + ENEMY_LEVEL_BOUNDS_BUFFER, _height - Constants.TOP_OF_SCREEN_BUFFER - ENEMY_LEVEL_BOUNDS_BUFFER);
        PositionComponent positionComponent = new PositionComponent(xPos, yPos);

        Sprite sprite = new Sprite(ResourceManager.getTexture("asteroid_large"));

        SpriteComponent spriteComponent = new SpriteComponent(sprite);
        BodyComponent bodyComponent = new BodyComponent(positionComponent, BodyFactory.getInstance().generate(entity, "asteroid_large.json", new Vector2(xPos, yPos)));
        EnemyDataComponent enemyDataComponent = new EnemyDataComponent(5); // TODO: point values
        RenderComponent renderComponent = new RenderComponent(0);

        entity.add(positionComponent).add(spriteComponent).add(bodyComponent).add(enemyDataComponent).add(renderComponent);

        EntityManager.getInstance().addEntity(entity);
    }

    private float getRandomFloat(float start, float end) {
        return start + ((end - start) * _rand.nextFloat());
    }

    public float getMinGameboardX() {
        return 0f;
    }

    public float getMinGameboardY() {
        return Constants.TOP_OF_SCREEN_BUFFER;
    }

    public float getMaxGameboardX() {
        return _width;
    }

    public float getMaxGameboardY() {
        return _height;
    }

}

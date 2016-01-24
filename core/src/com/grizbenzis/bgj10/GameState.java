package com.grizbenzis.bgj10;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
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
    private float _alienSpawnTimer;
    private float _powerupSpawnTimer;
    private float _blackHoleTimer;
    private Random _rand;
    private int _enemyCount;
    private int _alienCount;
    public void addEnemy(int count) {
        _enemyCount += count;
    }
    public void removeEnemy(int count) {
        _enemyCount -= count;
    }
    public void addAlien() {
        ++_alienCount;
    }
    public void removeAlien() {
        --_alienCount;
    }

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

        _lives = Constants.INITIAL_LIVES_COUNT;

        _enemySpawnTimer = 0f;
        _enemyCount = 0;

        _alienSpawnTimer = 0f;
        _alienCount = 0;

        _blackHoleTimer = getBlackholeTimer();
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
            _alienSpawnTimer -= (float) Time.time;
            _levelTimer -= (float) Time.time;
            _powerupSpawnTimer -= (float) Time.time;
        }

        if((_enemySpawnTimer < 0f) && (_enemyCount < getMaxEnemies())) {
            _enemySpawnTimer = getSpawnTimer();
            spawnLargeAsteroid();
        }
        if (_alienSpawnTimer < 0f && _alienCount < getMaxAliens()) {
            _alienSpawnTimer = getAlienSpawnTimer();
            spawnAlien();
        }
        if(_levelTimer < 0f) {
            ++_level;
            _levelTimer = Constants.TIME_PER_LEVEL;
        }
        if(_powerupSpawnTimer < 0f) {
            _powerupSpawnTimer = Constants.TIME_BETWEEN_POWERUPS;
            spawnPowerup();
        }
        if(_level >= Constants.BLACK_HOLE_MIN_LEVEL) {
            _blackHoleTimer -= Time.time;
            if(_blackHoleTimer < 0f) {
                _blackHoleTimer = getBlackholeTimer();
//                spawnBlackHole();
            }
        }
    }

    private final float MIN_SPAWN_INCREASE_RATE_HACK = 0.4f;
    private final float MAX_SPAWN_INCREASE_RATE_HACK = 0.6f;
    private final float SPAWN_INCREASE_HACK_MOD = 0.01f;
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

    private final float getAlienSpawnTimer() {
        return Constants.ALIEN_DEFAULT_SPAWN_TIMER;
    }

    private final float getBlackholeTimer() {
        return Constants.BLACK_HOLE_DEFAULT_SPAWN_TIMER;
    }

    private final float POWERUP_LEVEL_BOUNDS_BUFFER = 100f;
    public void spawnPowerup() {
        Entity entity = new Entity();

        int type = _rand.nextInt(Constants.PowerupType.values().length);

        float xPos = getRandomFloat(0f + POWERUP_LEVEL_BOUNDS_BUFFER, _width - POWERUP_LEVEL_BOUNDS_BUFFER);
        float yPos = getRandomFloat(0f + POWERUP_LEVEL_BOUNDS_BUFFER, _height - Constants.TOP_OF_SCREEN_BUFFER - POWERUP_LEVEL_BOUNDS_BUFFER);
        PositionComponent positionComponent = new PositionComponent(xPos, yPos);

        Sprite sprite = null;
        if(type == Constants.PowerupType.EXPLOSION_UP.ordinal())
            sprite = new Sprite(ResourceManager.getTexture("powerup_expl"));
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

    public void spawnLargeAsteroid() {
        Entity entity = new Entity();

        Vector2[] posAndVel = initPosAndVel();

        PositionComponent positionComponent = new PositionComponent(posAndVel[0]);

        Sprite sprite = new Sprite(ResourceManager.getTexture("asteroid_large"));

        SpriteComponent spriteComponent = new SpriteComponent(sprite);
        BodyComponent bodyComponent = new BodyComponent(positionComponent, BodyFactory.getInstance().generate(entity, "asteroid_large.json", posAndVel[0]));
        EnemyDataComponent enemyDataComponent = new EnemyDataComponent(Constants.EnemyType.ASTEROID_LARGE);
        RenderComponent renderComponent = new RenderComponent(0);

        Vector2 impulse = posAndVel[1].scl(Constants.LARGE_ASTEROID_SPAWN_SPEED_FACTOR * bodyComponent.body.getMass());
        bodyComponent.body.applyLinearImpulse(impulse.x, impulse.y, bodyComponent.body.getWorldCenter().x, bodyComponent.body.getWorldCenter().y, true);

        float torque = getRandomFloat(0.5f, 1f);
        if(_rand.nextBoolean())
            torque *= -1f;
        torque *= Constants.LARGE_ASTEROID_TORQUE_FACTOR;
        Gdx.app.log(Constants.LOG_TAG, "ASTEROID TORQUE:" + torque);
        bodyComponent.body.applyTorque(torque, true);

        entity.add(positionComponent).add(spriteComponent).add(bodyComponent).add(enemyDataComponent).add(renderComponent);

        EntityManager.getInstance().addEntity(entity);
    }

    public void spawnMediumAsteroid(Vector2 startPos, Vector2 startVel) {
        Entity entity = new Entity();

        PositionComponent positionComponent = new PositionComponent(startPos);
        float deltaX = getRandomFloat(0.5f, 1f);
        if(_rand.nextBoolean())
            deltaX *= -1;
        float deltaY = getRandomFloat(0.5f, 1f);
        if(_rand.nextBoolean())
            deltaY *= -1;
        Vector2 deltaVel = new Vector2(deltaX, deltaY).scl(Constants.MEDIUM_ASTEROID_SPAWN_SPEED_FACTOR);
        startVel = deltaVel.add(startVel);

        Sprite sprite = new Sprite(ResourceManager.getTexture("asteroid_medium"));

        SpriteComponent spriteComponent = new SpriteComponent(sprite);
        BodyComponent bodyComponent = new BodyComponent(positionComponent, BodyFactory.getInstance().generate(entity, "asteroid_medium.json", startPos));
        EnemyDataComponent enemyDataComponent = new EnemyDataComponent(Constants.EnemyType.ASTEROID_MEDIUM);
        RenderComponent renderComponent = new RenderComponent(0);

        Vector2 impulse = startVel.scl(bodyComponent.body.getMass());
        bodyComponent.body.applyLinearImpulse(impulse.x, impulse.y, bodyComponent.body.getWorldCenter().x, bodyComponent.body.getWorldCenter().y, true);

        float torque = getRandomFloat(0.5f, 1f);
        if(_rand.nextBoolean())
            torque *= -1f;
        torque *= Constants.MEDIUM_ASTEROID_TORQUE_FACTOR;
        bodyComponent.body.applyTorque(torque, true);

        entity.add(positionComponent).add(spriteComponent).add(bodyComponent).add(enemyDataComponent).add(renderComponent);

        EntityManager.getInstance().addEntity(entity);
    }

    public void spawnSmallAsteroid(Vector2 startPos, Vector2 startVel) {
        Entity entity = new Entity();

        PositionComponent positionComponent = new PositionComponent(startPos);
        float deltaX = getRandomFloat(0.5f, 1f);
        if(_rand.nextBoolean())
            deltaX *= -1;
        float deltaY = getRandomFloat(0.5f, 1f);
        if(_rand.nextBoolean())
            deltaY *= -1;
        Vector2 deltaVel = new Vector2(deltaX, deltaY).scl(Constants.SMALL_ASTEROID_SPAWN_SPEED_FACTOR);
        startVel = deltaVel.add(startVel);

        Sprite sprite = new Sprite(ResourceManager.getTexture("asteroid_small"));

        SpriteComponent spriteComponent = new SpriteComponent(sprite);
        sprite.rotate(getRandomFloat(0f, 360f));

        BodyComponent bodyComponent = new BodyComponent(positionComponent, BodyFactory.getInstance().generate(entity, "asteroid_small.json", startPos));
        EnemyDataComponent enemyDataComponent = new EnemyDataComponent(Constants.EnemyType.ASTEROID_SMALL);
        RenderComponent renderComponent = new RenderComponent(0);

        Vector2 impulse = startVel.scl(bodyComponent.body.getMass());
        bodyComponent.body.applyLinearImpulse(impulse.x, impulse.y, bodyComponent.body.getWorldCenter().x, bodyComponent.body.getWorldCenter().y, true);

        float torque = getRandomFloat(0.5f, 1f);
        if(_rand.nextBoolean())
            torque *= -1f;
        torque *= Constants.SMALL_ASTEROID_TORQUE_FACTOR;
        bodyComponent.body.applyTorque(torque, true);

        entity.add(positionComponent).add(spriteComponent).add(bodyComponent).add(enemyDataComponent).add(renderComponent);

        EntityManager.getInstance().addEntity(entity);
    }

    public void spawnAlien() {
        Entity entity = new Entity();

        Vector2[] posAndVel = initAlienPosAndVel();

        PositionComponent positionComponent = new PositionComponent(posAndVel[0]);

        Sprite sprite = new Sprite(ResourceManager.getTexture("alien"));

        SpriteComponent spriteComponent = new SpriteComponent(sprite);
        BodyComponent bodyComponent = new BodyComponent(positionComponent, BodyFactory.getInstance().generate(entity, "alien.json", posAndVel[0]));
        EnemyDataComponent enemyDataComponent = new EnemyDataComponent(Constants.EnemyType.ALIEN);
        RenderComponent renderComponent = new RenderComponent(0);

        Vector2 impulse = posAndVel[1].scl(Constants.ALIEN_SPAWN_SPEED_FACTOR * bodyComponent.body.getMass());
        bodyComponent.body.applyLinearImpulse(impulse.x, impulse.y, bodyComponent.body.getWorldCenter().x, bodyComponent.body.getWorldCenter().y, true);
        bodyComponent.body.setFixedRotation(true);

        entity.add(positionComponent).add(spriteComponent).add(bodyComponent).add(enemyDataComponent).add(renderComponent);

        EntityManager.getInstance().addEntity(entity);
    }

    private void spawnBlackHole() {
        Entity entity = new Entity();

        Vector2 pos = new Vector2();
        float curDistance = -1f;
        while(curDistance < Constants.BLACK_HOLE_POS_BUFFER) {
            pos.x = getRandomFloat(Constants.BLACK_HOLE_POS_BUFFER, _width - Constants.BLACK_HOLE_POS_BUFFER);
            pos.y = getRandomFloat(Constants.BLACK_HOLE_POS_BUFFER, _height - Constants.BLACK_HOLE_POS_BUFFER);
            curDistance = _player.getCenterPos().dst(pos);
        }

        Sprite sprite = new Sprite(ResourceManager.getTexture("black_hole"));
        SpriteComponent spriteComponent = new SpriteComponent(sprite);
        sprite.setCenter(pos.x, pos.y);

        Vector2 position = new Vector2(pos.x, pos.y);

        PositionComponent positionComponent = new PositionComponent(position);
        BodyComponent bodyComponent = new BodyComponent(positionComponent, BodyFactory.getInstance().generate(entity, "black_hole.json", position));

        entity.add(new BlackHoleComponent()).add(new RenderComponent(0)).add(spriteComponent).add(positionComponent).add(bodyComponent);

        EntityManager.getInstance().addEntity(entity);
    }

    private float getRandomFloat(float start, float end) {
        return start + ((end - start) * _rand.nextFloat());
    }
    private int getRandomInt(int start, int end) {
        return _rand.nextInt(end - start) + start;
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

    private Vector2[] initPosAndVel() {
        Vector2[] retval = new Vector2[2];
        float minorVelComponent = getRandomFloat(-0.6f, 0.6f);
        Vector2 startPos = null;
        Vector2 startVel = null;
        switch(getRandomInt(0, 4)) {
            case 0: { // TOP
                startPos = new Vector2(getRandomFloat(0f, _width), _height);
                startVel = new Vector2(minorVelComponent, getRandomFloat(-0.5f, -1f));
                break;
            }
            case 1: { // RIGHT
                startPos = new Vector2(_width, getRandomFloat(0f, _height - Constants.TOP_OF_SCREEN_BUFFER));
                startVel = new Vector2(getRandomFloat(-0.5f, -1f), minorVelComponent);
                break;
            }
            case 2: { // BOTTOM
                startPos = new Vector2(getRandomFloat(0f, _width), 0f);
                startVel = new Vector2(minorVelComponent, getRandomFloat(0.5f, 1f));
                break;
            }
            case 3: { // LEFT
                startPos = new Vector2(0f, getRandomFloat(0f, _height - Constants.TOP_OF_SCREEN_BUFFER));
                startVel = new Vector2(getRandomFloat(0.5f, 1f), minorVelComponent);
                break;
            }
        }
        retval[0] = startPos;
        retval[1] = startVel;
        return retval;
    }

    private Vector2[] initAlienPosAndVel() {
        Vector2[] retval = new Vector2[2];
        Vector2 startPos = null;
        Vector2 startVel = null;
        switch(getRandomInt(0, 2)) {
            case 0: { // RIGHT
                startPos = new Vector2(_width, getRandomFloat(0f, _height - Constants.TOP_OF_SCREEN_BUFFER - 40));
                startVel = new Vector2(getRandomFloat(-0.5f, -1f), 0);
                break;
            }
            case 1: { // LEFT
                startPos = new Vector2(0f, getRandomFloat(0f, _height - Constants.TOP_OF_SCREEN_BUFFER - 40));
                startVel = new Vector2(getRandomFloat(0.5f, 1f), 0);
                break;
            }
        }
        retval[0] = startPos;
        retval[1] = startVel;
        return retval;
    }

    public int getMaxEnemies() {
        return (int)(((float)_level * 1.5f) + 5f);
//        switch(_level) {
//            case 1: return 5;
//            case 2: return 6;
//            case 3: return 7;
//            case 4: return 8;
//            case 5: return 9;
//            case 6: return 10;
//            case 7: return 11;
//            case 8: return 12;
//            case 9: return 13;
//        }
    }

    public int getMaxAliens() {
        return (int)(((float)_level * .5f));
    }

}

package com.grizbenzis.bgj10.screens;

import com.badlogic.ashley.core.Engine;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.grizbenzis.bgj10.*;
import com.grizbenzis.bgj10.actors.Player;
import com.grizbenzis.bgj10.components.PlayerDataComponent;
import com.grizbenzis.bgj10.systems.PlayerDataSystem;
import com.grizbenzis.bgj10.systems.PositionSystem;
import com.grizbenzis.bgj10.systems.PowerupSystem;
import com.grizbenzis.bgj10.systems.RenderSpriteSystem;

/**
 * Created by sponaas on 1/22/16.
 */
public class GameScreen implements Screen {

    private Engine _engine;
    private OrthographicCamera _camera;
    private World _world;
    private ContactManager _contactManager;

    private int _screenWidth, _screenHeight;
    private SpriteBatch _spriteBatch;
    private SpriteBatch _hudBatch;
    private ShapeRenderer _shapeRenderer;

    private Sprite _backgroundSprite;

    private InputManager _inputManager;

    private Box2DDebugRenderer _debugRenderer;


    @Override
    public void show() {
        _spriteBatch = new SpriteBatch();
        _hudBatch = new SpriteBatch();
        _debugRenderer = new Box2DDebugRenderer();

        _engine = initializeEngine();

        _world = new World(new Vector2(0f, 0f), false);
        BodyFactory.initialize(_world);
        _contactManager = new ContactManager(_engine, _world);

        _screenWidth = Gdx.graphics.getWidth();
        _screenHeight = Gdx.graphics.getHeight();

        _camera = new OrthographicCamera();
        _camera.setToOrtho(false, _screenWidth, _screenHeight);
        _camera.update();

        EntityManager.initialize(_engine, _world);

        _inputManager = new InputManager();
        InputMultiplexer inputMultiplexer = new InputMultiplexer();
        inputMultiplexer.addProcessor(_inputManager);
        Gdx.input.setInputProcessor(inputMultiplexer);

        _shapeRenderer = new ShapeRenderer();

        _backgroundSprite = new Sprite(ResourceManager.getTexture("background"));
        _backgroundSprite.setPosition(0f, 0f);
    }

    @Override
    public void render(float delta) {
        Time.update();
        _world.step(1f / 60f, 6, 2);
        Matrix4 debugMatrix = _spriteBatch.getProjectionMatrix().cpy().scale(Constants.METERS_TO_PIXELS, Constants.METERS_TO_PIXELS, 0);

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        _camera.position.x = _screenWidth / 2f;
        _camera.position.y = _screenHeight / 2f;
        _camera.update();

        GameState.getInstance().update();

        _spriteBatch.begin();
        _backgroundSprite.draw(_spriteBatch);
        _engine.update((float) Time.time);
        _spriteBatch.setProjectionMatrix(_camera.combined);
        renderActivePowerups();
        _spriteBatch.end();

        renderHud();

        _shapeRenderer.setProjectionMatrix(_camera.combined);
        renderShotCharges();
        _shapeRenderer.end();

        EntityManager.getInstance().update();

//        _debugRenderer.render(_world, debugMatrix);
    }

    @Override
    public void resize(int width, int height) {
        _screenWidth = width;
        _screenHeight = height;

        GameState.initialize(_screenWidth, _screenHeight);

        Player player = Player.makePlayer();
        EntityManager.getInstance().addEntity(player.getEntity());
        EntityManager.getInstance().addActor(player);
        GameState.getInstance().setPlayer(player);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }

    public Engine initializeEngine() {
        Engine engine = new Engine();

        PositionSystem positionSystem = new PositionSystem(0);
        RenderSpriteSystem renderSpriteSystem = new RenderSpriteSystem(_spriteBatch, 1);
        PlayerDataSystem playerDataSystem = new PlayerDataSystem(2);
        PowerupSystem powerupSystem = new PowerupSystem(3);

        engine.addSystem(positionSystem);
        engine.addSystem(renderSpriteSystem);
        engine.addSystem(playerDataSystem);
        engine.addSystem(powerupSystem);

        return engine;
    }

    private void renderHud() {
        BitmapFont hudFont = ResourceManager.getHudFont();

        _hudBatch.begin();
        float scoreIconXPos = 4f; // lil bit of padding here...
        int level = GameState.getInstance().getLevel();
        int score = GameState.getInstance().getScore();
        int lives = GameState.getInstance().getLives();
        hudFont.draw(_hudBatch, "LIVES: " + lives + "   LEVEL: " + level + "   SCORE: " + score, scoreIconXPos, (float) Gdx.graphics.getHeight() - 4f); // TODO: actually show the score
        _hudBatch.end();
    }

    private final float POWERUP_BUFFER_HACK = 25f;

    private void renderActivePowerups() {
        float drawPosX = GameState.getInstance().getWidth() - POWERUP_BUFFER_HACK;
        float drawPosY = GameState.getInstance().getHeight() - POWERUP_BUFFER_HACK + 3f;

        PlayerDataComponent playerData = GameState.getInstance().getPlayerData();
        if (playerData.spreadShotTime > 0f) {
            _spriteBatch.draw(ResourceManager.getTexture("powerup_sprd_small"), drawPosX, drawPosY);
            drawPosX -= POWERUP_BUFFER_HACK;
        }
        if (playerData.rapidShotTime > 0f) {
            _spriteBatch.draw(ResourceManager.getTexture("powerup_rpd_small"), drawPosX, drawPosY);
            drawPosX -= POWERUP_BUFFER_HACK;
        }
        if (playerData.points2xTime > 0f) {
            _spriteBatch.draw(ResourceManager.getTexture("powerup_2x_small"), drawPosX, drawPosY);
        }
    }

    private static float SHOT_CHARGE_HACK = 0f;
    private void renderShotCharges() {
        Player player = GameState.getInstance().getPlayer();
        Vector2 pos = player.getCenterPos();

        pos.y += 50f; //TODO: WARNING! Magic number
        float width = 50f; //TODO: WARNING! Magic number
        float height = 10f; //TODO: WARNING! Magic number
        float buffer = 1f; //TODO: WARNING! Magic number

        Constants.WeaponState weaponState = player.getWeaponState();
        if (weaponState == Constants.WeaponState.CHARGING) {
            SHOT_CHARGE_HACK = player.getWeaponCharge();
            _shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            _shapeRenderer.setColor(new Color(0f, 0f, 0f, 0.3f));
            _shapeRenderer.rect(pos.x - (width / 2f), pos.y, width, height);
            _shapeRenderer.end();

            _shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            _shapeRenderer.setColor(new Color(1f, 0f, 0f, 0.5f));
            _shapeRenderer.rect(pos.x - (width / 2f) + buffer, pos.y + buffer, width - (2f * buffer), height - (2f * buffer));
            _shapeRenderer.end();

            _shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            _shapeRenderer.setColor(new Color(0f, 1f, 0f, 0.8f));
            _shapeRenderer.rect(pos.x - (width / 2f) + buffer, pos.y + buffer, SHOT_CHARGE_HACK * width - (2f * buffer), (height - (2f * buffer)));
            _shapeRenderer.end();
        } else if (weaponState == Constants.WeaponState.COOL_DOWN) {
            _shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            _shapeRenderer.setColor(new Color(0f, 0f, 0f, 0.2f));
            _shapeRenderer.rect(pos.x - (width / 2f), pos.y, width, height);
            _shapeRenderer.end();

            _shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            _shapeRenderer.setColor(new Color(0.3f, 0f, 0f, 0.2f));
            _shapeRenderer.rect(pos.x - (width / 2f) + buffer, pos.y + buffer, width - (2f * buffer), height - (2f * buffer));
            _shapeRenderer.end();

            _shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            _shapeRenderer.setColor(new Color(0f, 0.3f, 0f, 0.2f));
            _shapeRenderer.rect(pos.x - (width / 2f) + buffer, pos.y + buffer, SHOT_CHARGE_HACK * width - (2f * buffer), (height - (2f * buffer)));
            _shapeRenderer.end();
        }
    }
}

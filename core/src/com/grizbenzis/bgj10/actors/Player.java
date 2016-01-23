package com.grizbenzis.bgj10.actors;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.grizbenzis.bgj10.*;
import com.grizbenzis.bgj10.components.*;
import com.grizbenzis.bgj10.screens.GameOverScreen;

/**
 * Created by sponaas on 1/22/16.
 */
public class Player extends Actor {

    private float _shotTimer;
    private ComponentMapper<PlayerDataComponent> _playerDataComponents = ComponentMapper.getFor(PlayerDataComponent.class);
    private ComponentMapper<BodyComponent> _bodyComponents = ComponentMapper.getFor(BodyComponent.class);

    public Player(Entity entity) {
        super(entity);
    }

    public static Player makePlayer() {
        Entity entity = new Entity();

        Sprite sprite = new Sprite(ResourceManager.getTexture("player"));
        SpriteComponent spriteComponent = new SpriteComponent(sprite);
        Vector2 position = new Vector2(GameState.getInstance().getWidth() / 2, GameState.getInstance().getHeight() / 2);

        Body body = BodyFactory.getInstance().generate(entity, "player.json", position);

        PositionComponent playerPositionComponent = new PositionComponent(position.x, position.y);
        BodyComponent playerBodyComponent = new BodyComponent(playerPositionComponent, body);
        RenderComponent renderComponent = new RenderComponent(0);
        PlayerDataComponent playerDataComponent = new PlayerDataComponent();

        entity.add(playerPositionComponent).add(playerBodyComponent).add(spriteComponent).add(renderComponent).add(playerDataComponent);

        Player player = new Player(entity);
        player.setSizeXInPixels(sprite.getWidth());
        player.setSizeYInPixels(sprite.getHeight());

        return player;
    }

    @Override
    public void update() {
        PlayerDataComponent playerDataComponent = _playerDataComponents.get(getEntity());
        if(playerDataComponent.alive) {
            updateRotation();
            updateShooting();
            playerDataComponent.invincibilityTime -= (float)Time.time;
            if(playerDataComponent.invincibilityTime < -1f)
                playerDataComponent.invincibilityTime = -1f;
            if(playerDataComponent.invincibilityTime < 0.01f) {
                BodyComponent bodyComponent = _bodyComponents.get(getEntity());
                Fixture fixture = bodyComponent.body.getFixtureList().get(0);
                Filter filter = fixture.getFilterData();
                filter.maskBits = Constants.BITMASK_LEVEL_BOUNDS | Constants.BITMASK_ENEMY | Constants.BITMASK_POWERUP;
                fixture.setFilterData(filter);
                playerDataComponent.invincibilityTime = -1f;
            }
        }
        else {
            playerDataComponent.playerDeathTime -= (float) Time.time;
            if(playerDataComponent.playerDeathTime < 0f) {
                if(GameState.getInstance().getLives() > 0) {
                    GameState.getInstance().decrementLives();
                    respawn();
                }
                else {
                    bgj10.game.setScreen(new GameOverScreen());
                }
            }
        }
    }

    private void updateShooting() {
        // TODO: IMPLEMENT ME
    }

    private void updateRotation() {
        float rotationInput = 0f;
        if(InputManager.rotateLeftActive) {
            rotationInput += 1f;
        }
        if(InputManager.rotateRightActive) {
            rotationInput -= 1f;
        }

        float rotationAdjustment = rotationInput * Constants.PLAYER_ROTATION_SPEED * (float) Time.time;

        Body body = getBody();
        float rotation = body.getAngle();

        rotation += rotationAdjustment;
        if(rotation >= Constants.TWO_PI) {
            rotation -= Constants.TWO_PI;
        }
        else if(rotation <= 0f) {
            rotation += Constants.TWO_PI;
        }

        body.setTransform(body.getPosition(), rotation);
    }

    private void respawn() {
        EntityManager.getInstance().destroyEntity(getEntity());
        EntityManager.getInstance().removeActor(this);

        Player player = Player.makePlayer();
        EntityManager.getInstance().addEntity(player.getEntity());
        EntityManager.getInstance().addActor(player);
        GameState.getInstance().setPlayer(player);
    }

}

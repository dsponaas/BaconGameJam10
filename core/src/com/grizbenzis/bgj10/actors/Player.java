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

    private Constants.WeaponState _weaponState;
    private float _weaponTimer;

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

        player._weaponState = Constants.WeaponState.READY;

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

    private void updateShooting() {
        _weaponTimer -= (float)Time.time;
        switch(_weaponState) {
            case READY: {
                if(InputManager.shootingActive) {
                    _weaponState = Constants.WeaponState.CHARGING;
                    _weaponTimer = 5f; // TODO: IMPLEMENT ME FOR REAL
                }
                break;
            }
            case CHARGING: {
                if(!InputManager.shootingActive) {
                    fire();
                    _weaponState = Constants.WeaponState.COOL_DOWN;
                    _weaponTimer = 5f; // TODO: IMPLEMENT ME FOR REAL
                }
                break;
            }
            case COOL_DOWN: {
                if(_weaponTimer < 0f) {
                    _weaponState = Constants.WeaponState.READY;
                }
                break;
            }
        }
    }

    private void fire() {
        PositionComponent positionComponent = getPosition();
        Vector2 pos = new Vector2(positionComponent.x + (getSizeXInPixels() / 2), positionComponent.y + (getSizeYInPixels() / 2));
        float rotation = positionComponent.rotation;

        rotation += Constants.ONE_AND_A_HALF_PI;
        if(rotation > Constants.TWO_PI) {
            rotation -= Constants.TWO_PI;
        }
        else if(rotation < 0f) {
            rotation += Constants.TWO_PI;
        }

        float power = 1f; // TODO: set power for real
        shoot(pos.x, pos.y, rotation, power);
        if(GameState.getInstance().getPlayerData().spreadShotTime > 0f) {
            float leftShotRotation = rotation - Constants.SPREAD_SHOT_ANGLE;
            float rightShotRotation = rotation + Constants.SPREAD_SHOT_ANGLE;
            shoot(pos.x, pos.y, leftShotRotation, power);
            shoot(pos.x, pos.y, rightShotRotation, power);
        }

        Vector2 deltaVelocity = (new Vector2(0f, 1f)).rotateRad(positionComponent.rotation).scl(power);
        Vector2 impulse = deltaVelocity.scl(getBody().getMass());
        getBody().applyLinearImpulse(impulse.x, impulse.y, getBody().getWorldCenter().x, getBody().getWorldCenter().y, true);
    }

    private void shoot( float posX, float posY, float angle, float power ) {
        Entity bulletEntity = new Entity();
        SpriteComponent bulletSprite = new SpriteComponent(new Sprite(ResourceManager.getTexture("bullet")));

        PositionComponent bulletPosition = new PositionComponent(posX, posY);
        Body body = BodyFactory.getInstance().generate(bulletEntity, "bullet.json", new Vector2(posX, posY));
        BodyComponent bulletBody = new BodyComponent(bulletPosition, body);
        RenderComponent renderComponent = new RenderComponent(0);

        bulletEntity.add(bulletSprite).add(bulletPosition).add(bulletBody).add(renderComponent);
        EntityManager.getInstance().addEntity(bulletEntity);

        float velX = (float)Math.cos(angle);
        float velY = (float)Math.sin(angle);

        velX *= Constants.BULLET_SPEED * power;
        velY *= Constants.BULLET_SPEED * power;
        body.applyLinearImpulse(velX, velY, body.getWorldCenter().x, body.getWorldCenter().y, true);
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

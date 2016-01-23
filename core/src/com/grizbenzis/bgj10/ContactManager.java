package com.grizbenzis.bgj10;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.grizbenzis.bgj10.components.*;

/**
 * Created by sponaas on 1/22/16.
 */
public class ContactManager implements ContactListener {

    private Engine _engine;
    private World _world;
    private ComponentMapper<BodyComponent> _bodyComponents = ComponentMapper.getFor(BodyComponent.class);
    private ComponentMapper<SpriteComponent> _spriteComponents = ComponentMapper.getFor(SpriteComponent.class);
    private ComponentMapper<PlayerDataComponent> _playerDataComponents = ComponentMapper.getFor(PlayerDataComponent.class);
    private ComponentMapper<PositionComponent> _positionComponents = ComponentMapper.getFor(PositionComponent.class);

    public ContactManager(Engine engine, World world) {
        _engine = engine;
        _world = world;
        _world.setContactListener(this);
    }

    @Override
    public void beginContact(Contact contact) {
        Fixture fixtureA = contact.getFixtureA();
        Fixture fixtureB = contact.getFixtureB();
        Body bodyA = fixtureA.getBody();
        Body bodyB = fixtureB.getBody();
        short fixtureAType = fixtureA.getFilterData().categoryBits;
        short fixtureBType = fixtureB.getFilterData().categoryBits;
        Entity entityA = (Entity)fixtureA.getUserData();
        Entity entityB = (Entity)fixtureB.getUserData();

        if((Constants.BITMASK_PLAYER_BULLET == fixtureAType) && (Constants.BITMASK_LEVEL_BOUNDS == fixtureBType)) {
            Gdx.app.log(Constants.LOG_TAG, "CRAPTAR!");
            EntityManager.getInstance().destroyEntity(entityA);
        }
        else if((Constants.BITMASK_PLAYER_BULLET == fixtureBType) && (Constants.BITMASK_LEVEL_BOUNDS == fixtureAType)) {
            Gdx.app.log(Constants.LOG_TAG, "CRAPTAR!");
            EntityManager.getInstance().destroyEntity(entityB);
        }

        else if((Constants.BITMASK_PLAYER_BULLET == fixtureAType) && (Constants.BITMASK_ENEMY == fixtureBType)) {
            EnemyDataComponent enemyDataComponent = (EnemyDataComponent)entityB.getComponent(EnemyDataComponent.class);
            if(null != enemyDataComponent)
                killEnemy(entityB, bodyB, fixtureB, enemyDataComponent);
            EntityManager.getInstance().destroyEntity(entityA);
        }
        else if((Constants.BITMASK_PLAYER_BULLET == fixtureBType) && (Constants.BITMASK_ENEMY == fixtureAType)) {
            EnemyDataComponent enemyDataComponent = (EnemyDataComponent)entityA.getComponent(EnemyDataComponent.class);
            if(null != enemyDataComponent)
                killEnemy(entityA, bodyA, fixtureA, enemyDataComponent);
            EntityManager.getInstance().destroyEntity(entityB);
        }

        else if((Constants.BITMASK_PLAYER == fixtureAType) && (Constants.BITMASK_ENEMY == fixtureBType)) {
            PlayerDataComponent playerDataComponent = _playerDataComponents.get(entityA);
//            if(playerDataComponent.alive && (playerDataComponent.invincibilityTime < 0f))
//                killPlayer(entityA, bodyA, fixtureA, playerDataComponent);
        }
        else if((Constants.BITMASK_PLAYER == fixtureBType) && (Constants.BITMASK_ENEMY == fixtureAType)) {
            PlayerDataComponent playerDataComponent = _playerDataComponents.get(entityB);
//            if(playerDataComponent.alive && (playerDataComponent.invincibilityTime < 0f))
//                killPlayer(entityB, bodyB, fixtureB, playerDataComponent);
        }

        else if((Constants.BITMASK_POWERUP == fixtureAType) && (Constants.BITMASK_PLAYER == fixtureBType)) {
            PowerupComponent powerupComponent = entityA.getComponent(PowerupComponent.class);
            powerupComponent.pickedUp = true;
        }
        else if((Constants.BITMASK_POWERUP == fixtureBType) && (Constants.BITMASK_PLAYER == fixtureAType)) {
            PowerupComponent powerupComponent = entityB.getComponent(PowerupComponent.class);
            powerupComponent.pickedUp = true;
        }
    }

    private void killEnemy(Entity entity, Body body, Fixture fixture, EnemyDataComponent enemyDataComponent) {
        Filter filter = fixture.getFilterData();
        filter.maskBits = 0;
        fixture.setFilterData(filter);

        enemyDataComponent.alive = false;
        Gdx.app.log(Constants.LOG_TAG, "DEAD ENEMY");
    }

    private void killPlayer(Entity entity, Body body, Fixture fixture, PlayerDataComponent playerDataComponent) {
        Filter filter = fixture.getFilterData();
        filter.maskBits = Constants.BITMASK_LEVEL_BOUNDS | Constants.BITMASK_POWERUP;
        fixture.setFilterData(filter);

        SpriteComponent spriteComponent = _spriteComponents.get(entity);
        spriteComponent.sprite = new Sprite(ResourceManager.getTexture("deadplayer"));

        Vector2 velocity = body.getLinearVelocity();
        Vector2 desiredVelocity = new Vector2(0f, 0f);
        desiredVelocity.sub(velocity).scl(body.getMass());
        body.applyLinearImpulse(desiredVelocity.x, desiredVelocity.y, body.getWorldCenter().x, body.getWorldCenter().y, true);

        playerDataComponent.alive = false;
        playerDataComponent.playerDeathTime = Constants.PLAYER_DEATH_TIME;
    }

    @Override
    public void endContact(Contact contact) {}

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {}

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {}
}

package com.grizbenzis.bgj10.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.grizbenzis.bgj10.Constants;
import com.grizbenzis.bgj10.EntityManager;
import com.grizbenzis.bgj10.GameState;
import com.grizbenzis.bgj10.components.*;
import com.sun.tools.javac.code.Attribute;

/**
 * Created by sponaas on 1/23/16.
 */
public class EnemyDeathSystem extends IteratingSystem {

    private ComponentMapper<EnemyDataComponent> _enemyDataComponents = ComponentMapper.getFor(EnemyDataComponent.class);
    private ComponentMapper<PositionComponent> _positionComponents = ComponentMapper.getFor(PositionComponent.class);
    private ComponentMapper<BodyComponent> _bodyComponents = ComponentMapper.getFor(BodyComponent.class);

    public EnemyDeathSystem(int priority) {
        super(Family.all(EnemyDataComponent.class).get(), priority);
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        EnemyDataComponent enemyDataComponent = _enemyDataComponents.get(entity);
        if(!enemyDataComponent.alive) {
            Gdx.app.log(Constants.LOG_TAG, "DEAD ENEMY");

            entity.remove(RenderComponent.class);

            if(enemyDataComponent.type == Constants.EnemyType.ASTEROID_LARGE) {
                PositionComponent positionComponent = _positionComponents.get(entity);
                BodyComponent bodyComponent = _bodyComponents.get(entity);
                Vector2 startPos = new Vector2(positionComponent.x, positionComponent.y);

                if(enemyDataComponent.type == Constants.EnemyType.ASTEROID_LARGE) {
                    for(int i = 0; i < Constants.MEDIUM_ASTEROID_SPAWN_QUANTITY; ++i) {
                        GameState.getInstance().spawnMediumAsteroid(startPos, bodyComponent.body.getLinearVelocity());
                    }
                }
                else if(enemyDataComponent.type == Constants.EnemyType.ASTEROID_MEDIUM) {
                    for(int i = 0; i < Constants.SMALL_ASTEROID_SPAWN_QUANTITY; ++i) {
                        GameState.getInstance().spawnSmallAsteroid(startPos, bodyComponent.body.getLinearVelocity());
                    }
                }

//                Vector2 velocity = bodyComponent.body.getLinearVelocity();
//                Vector2 desiredVelocity = new Vector2(0f, 0f);
//                desiredVelocity.sub(velocity).scl(bodyComponent.body.getMass());
//                bodyComponent.body.applyLinearImpulse(desiredVelocity.x, desiredVelocity.y, bodyComponent.body.getWorldCenter().x, bodyComponent.body.getWorldCenter().y, true);

                GameState.getInstance().incrementScore(enemyDataComponent.getPoints());

                EntityManager.getInstance().destroyEntity(entity);
            }
        }
    }
}

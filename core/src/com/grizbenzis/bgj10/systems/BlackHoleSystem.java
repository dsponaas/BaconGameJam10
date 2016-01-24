package com.grizbenzis.bgj10.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.grizbenzis.bgj10.Constants;
import com.grizbenzis.bgj10.EntityManager;
import com.grizbenzis.bgj10.Time;
import com.grizbenzis.bgj10.components.*;
import com.grizbenzis.bgj10.screens.GameScreen;

/**
 * Created by sponaas on 1/24/16.
 */
public class BlackHoleSystem extends IteratingSystem {

    private ComponentMapper<BlackHoleComponent> _blackHoleComponents = ComponentMapper.getFor(BlackHoleComponent.class);
    private ComponentMapper<PositionComponent> _positionComponents = ComponentMapper.getFor(PositionComponent.class);
    private ComponentMapper<BodyComponent> _bodyComponents = ComponentMapper.getFor(BodyComponent.class);

    public BlackHoleSystem(int priority) {
        super(Family.all(BlackHoleComponent.class).get(), priority);
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        BlackHoleComponent blackHoleComponent = _blackHoleComponents.get(entity);
        PositionComponent blackHolePositionComponent = _positionComponents.get(entity);

        Vector2 blackHolePosition = new Vector2(blackHolePositionComponent.x, blackHolePositionComponent.y);

        --blackHoleComponent.lifeTimer;
        if(blackHoleComponent.lifeTimer < 0f) {
            entity.remove(BlackHoleComponent.class);
            entity.remove(RenderComponent.class);
            EntityManager.getInstance().destroyEntity(entity);
        }

        for(Entity otherEntity : GameScreen.getAllEntitiesWithPositionAndBody()) {
            PositionComponent otherPositionComponent = _positionComponents.get(otherEntity);
            Vector2 otherPosition = new Vector2(otherPositionComponent.x, otherPositionComponent.y);
            Vector2 deltaVector = new Vector2(blackHolePosition).sub(otherPosition);
            float deltaVectorLenSquared = deltaVector.len2();
            if(deltaVectorLenSquared < Constants.BLACK_HOLE_MAX_GRAV_DIST_SQUARED) {
                float factor = (Constants.BLACK_HOLE_MAX_GRAV_DIST_SQUARED - deltaVectorLenSquared) / Constants.BLACK_HOLE_MAX_GRAV_DIST_SQUARED;
                float gravForce = factor * Constants.BLACK_HOLE_MAX_GRAV;
                deltaVector = deltaVector.nor().scl(gravForce);

                BodyComponent bodyComponent = _bodyComponents.get(otherEntity);
                Vector2 impulse = deltaVector.scl(gravForce * (float)Time.time * bodyComponent.body.getMass());
                bodyComponent.body.applyLinearImpulse(impulse.x, impulse.y, bodyComponent.body.getWorldCenter().x, bodyComponent.body.getWorldCenter().y, true);
            }
        }
    }
}

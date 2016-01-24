package com.grizbenzis.bgj10.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
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
    private ComponentMapper<SpriteComponent> _spriteComponents = ComponentMapper.getFor(SpriteComponent.class);
    private ComponentMapper<PositionComponent> _positionComponents = ComponentMapper.getFor(PositionComponent.class);
    private ComponentMapper<BodyComponent> _bodyComponents = ComponentMapper.getFor(BodyComponent.class);
    private ComponentMapper<PowerupComponent> _powerUpComponents = ComponentMapper.getFor(PowerupComponent.class);

    public BlackHoleSystem(int priority) {
        super(Family.all(BlackHoleComponent.class).get(), priority);
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        BlackHoleComponent blackHoleComponent = _blackHoleComponents.get(entity);
        PositionComponent blackHolePositionComponent = _positionComponents.get(entity);

        Vector2 blackHolePosition = new Vector2(blackHolePositionComponent.x, blackHolePositionComponent.y);

        final float HACK_SPIN_FACTOR = 20f;
        SpriteComponent spriteComponent = _spriteComponents.get(entity);
        float newRotation = blackHolePositionComponent.rotation;
        newRotation += HACK_SPIN_FACTOR * Time.time;
        if(newRotation >= 360f) {
            newRotation -= 360f;
        }
        else if(newRotation < 0f) {
            newRotation += 360f;
        }
        blackHolePositionComponent.rotation = newRotation;
        spriteComponent.sprite.setRotation(newRotation);
        BodyComponent bodyComponent = _bodyComponents.get(entity);
        bodyComponent.body.applyTorque(HACK_SPIN_FACTOR, true);

        --blackHoleComponent.lifeTimer;
        if(blackHoleComponent.lifeTimer < 0f) {
            entity.remove(BlackHoleComponent.class);
            entity.remove(RenderComponent.class);
            EntityManager.getInstance().destroyEntity(entity);
        }

        for(Entity otherEntity : GameScreen.getAllEntitiesWithPositionAndBody()) {
            if(null != _powerUpComponents.get(otherEntity)) {
                continue;
            }
            PositionComponent otherPositionComponent = _positionComponents.get(otherEntity);
            Vector2 otherPosition = new Vector2(otherPositionComponent.x, otherPositionComponent.y);
            Vector2 deltaVector = new Vector2(blackHolePosition).sub(otherPosition);
            float deltaVectorLenSquared = deltaVector.len2();
            if(deltaVectorLenSquared < Constants.BLACK_HOLE_MAX_GRAV_DIST_SQUARED) {
                float factor = (Constants.BLACK_HOLE_MAX_GRAV_DIST_SQUARED - deltaVectorLenSquared) / Constants.BLACK_HOLE_MAX_GRAV_DIST_SQUARED;
                float gravForce = factor * Constants.BLACK_HOLE_MAX_GRAV;
                deltaVector = deltaVector.nor().scl(gravForce);

                BodyComponent otherBodyComponent = _bodyComponents.get(otherEntity);
                Vector2 impulse = deltaVector.scl(gravForce * (float) Time.time * otherBodyComponent.body.getMass());
                otherBodyComponent.body.applyLinearImpulse(impulse.x, impulse.y, otherBodyComponent.body.getWorldCenter().x, otherBodyComponent.body.getWorldCenter().y, true);
            }
        }
    }
}

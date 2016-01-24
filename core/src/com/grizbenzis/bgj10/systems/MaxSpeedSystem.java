package com.grizbenzis.bgj10.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.Vector2;
import com.grizbenzis.bgj10.Constants;
import com.grizbenzis.bgj10.components.BodyComponent;

/**
 * Created by sponaas on 1/24/16.
 */
public class MaxSpeedSystem extends IteratingSystem {

    private ComponentMapper<BodyComponent> _bodyComponents = ComponentMapper.getFor(BodyComponent.class);

    public MaxSpeedSystem(int priority) {
        super(Family.all(BodyComponent.class).get(), priority);
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        BodyComponent bodyComponent = _bodyComponents.get(entity);
        Vector2 velocity = bodyComponent.body.getLinearVelocity();
        float speed = velocity.len();
        if(speed > Constants.BODY_MAX_SPEED) {
            Vector2 velocityCorrection = new Vector2(velocity).nor().scl(-1f * bodyComponent.body.getMass() * (speed - Constants.BODY_MAX_SPEED));
            Vector2 impulse = velocityCorrection.scl(bodyComponent.body.getMass());
            bodyComponent.body.applyLinearImpulse(impulse.x, impulse.y, bodyComponent.body.getWorldCenter().x, bodyComponent.body.getWorldCenter().y, true);
        }
    }
}

package com.grizbenzis.bgj10.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.grizbenzis.bgj10.EntityManager;
import com.grizbenzis.bgj10.Time;
import com.grizbenzis.bgj10.components.ExplosionComponent;

/**
 * Created by sponaas on 6/13/15.
 */
public class ExplosionSystem extends IteratingSystem {

    private ComponentMapper<ExplosionComponent> _explosionComponents = ComponentMapper.getFor(ExplosionComponent.class);

    public ExplosionSystem(int priority) {
        super(Family.all(ExplosionComponent.class).get(), priority);
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        ExplosionComponent explosion = _explosionComponents.get(entity);
        explosion.timeLeft -= (float) Time.time;
        if(explosion.timeLeft < 0f)
            EntityManager.getInstance().destroyEntity(entity);
    }
}

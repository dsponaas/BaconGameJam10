package com.grizbenzis.bgj10.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.grizbenzis.bgj10.Constants;
import com.grizbenzis.bgj10.components.BodyComponent;
import com.grizbenzis.bgj10.components.PositionComponent;
import com.grizbenzis.bgj10.components.SpriteComponent;

/**
 * Created by sponaas on 1/22/16.
 */
public class PositionSystem extends IteratingSystem {

    private ComponentMapper<PositionComponent> _positionComponents = ComponentMapper.getFor(PositionComponent.class);
    private ComponentMapper<SpriteComponent> _spriteComponents = ComponentMapper.getFor(SpriteComponent.class);
    private ComponentMapper<BodyComponent> _bodyComponents = ComponentMapper.getFor(BodyComponent.class);

    public PositionSystem(int priority) {
        super(Family.all(PositionComponent.class).get(), priority);
    }

    public void processEntity(Entity entity, float deltaTime) {
        PositionComponent positionComponent = _positionComponents.get(entity);
        SpriteComponent spriteComponent = _spriteComponents.get(entity);
        BodyComponent bodyComponent = _bodyComponents.get(entity);

        float spriteHeight;
        float spriteWidth;

        spriteComponent.sprite.setX(positionComponent.x);
        spriteComponent.sprite.setY(positionComponent.y);

        spriteWidth = spriteComponent.sprite.getWidth();
        spriteHeight = spriteComponent.sprite.getHeight();

        if (bodyComponent != null) {
            positionComponent.x = (bodyComponent.body.getPosition().x * Constants.METERS_TO_PIXELS) - (spriteWidth / 2);
            positionComponent.y = (bodyComponent.body.getPosition().y * Constants.METERS_TO_PIXELS) - (spriteHeight / 2);
        }
    }

}

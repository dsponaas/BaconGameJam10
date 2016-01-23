package com.grizbenzis.bgj10.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.grizbenzis.bgj10.Constants;
import com.grizbenzis.bgj10.GameState;
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

        spriteWidth = spriteComponent.sprite.getWidth();
        spriteHeight = spriteComponent.sprite.getHeight();

        if (bodyComponent != null) {
            positionComponent.x = (bodyComponent.body.getPosition().x * Constants.METERS_TO_PIXELS) - (spriteWidth / 2);
            positionComponent.y = (bodyComponent.body.getPosition().y * Constants.METERS_TO_PIXELS) - (spriteHeight / 2);
            positionComponent.rotation = bodyComponent.body.getAngle();
        }

        spriteComponent.sprite.setX(positionComponent.x);
        spriteComponent.sprite.setY(positionComponent.y);
        spriteComponent.sprite.setRotation((float)Math.toDegrees(positionComponent.rotation));

        GameState gameState = GameState.getInstance();
        if(spriteComponent.sprite.getX() > gameState.getMaxGameboardX()) {
            float newX = (GameState.getInstance().getMinGameboardX() - (spriteWidth / 4)) * Constants.PIXELS_TO_METERS;
            bodyComponent.body.setTransform(newX, bodyComponent.body.getPosition().y, bodyComponent.body.getAngle());
        }
        else if((spriteComponent.sprite.getX() + spriteWidth) < gameState.getMinGameboardX()) {
            float newX = (GameState.getInstance().getMaxGameboardX() + (spriteWidth / 4)) * Constants.PIXELS_TO_METERS;
            bodyComponent.body.setTransform(newX, bodyComponent.body.getPosition().y, bodyComponent.body.getAngle());
        }
        if (spriteComponent.sprite.getY() > gameState.getMaxGameboardY()) {
            float newY = (GameState.getInstance().getMinGameboardY() - (spriteHeight / 4)) * Constants.PIXELS_TO_METERS;
            bodyComponent.body.setTransform(bodyComponent.body.getPosition().x, newY, bodyComponent.body.getAngle());
        }
        else if((spriteComponent.sprite.getY() + spriteHeight) < gameState.getMinGameboardY()) {
            float newY = (GameState.getInstance().getMaxGameboardY() + (spriteHeight / 4)) * Constants.PIXELS_TO_METERS;
            bodyComponent.body.setTransform(bodyComponent.body.getPosition().x, newY, bodyComponent.body.getAngle());
        }
    }

}

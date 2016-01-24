package com.grizbenzis.bgj10.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.SortedIteratingSystem;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.grizbenzis.bgj10.components.ParallaxBackgroundComponent;

/**
 * Created by sponaas on 1/23/16.
 */
public class ParallaxBackgroundRenderingSystem extends SortedIteratingSystem {
    private SpriteBatch _spriteBatch;
    private ComponentMapper<ParallaxBackgroundComponent> _backgroundComponents = ComponentMapper.getFor(ParallaxBackgroundComponent.class);

    public ParallaxBackgroundRenderingSystem(SpriteBatch spriteBatch, int priority) {
        super(Family.all(ParallaxBackgroundComponent.class).get(), new ParallaxBackgroundZComparator(), priority);
        _spriteBatch = spriteBatch;
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        ParallaxBackgroundComponent backgroundComponent = _backgroundComponents.get(entity);
        float deltaDist = deltaTime * backgroundComponent.scrollSpeed;
        backgroundComponent.positionX -= deltaDist;
        if(Math.abs(backgroundComponent.positionX) > backgroundComponent.sprite.getWidth())
            backgroundComponent.positionX += backgroundComponent.sprite.getWidth();

        backgroundComponent.sprite.setPosition(backgroundComponent.positionX, backgroundComponent.positionY);
        backgroundComponent.sprite.draw(_spriteBatch);

        backgroundComponent.sprite.setPosition(backgroundComponent.positionX + backgroundComponent.sprite.getWidth() + -1f, backgroundComponent.positionY); // TODO: CAREFUL! HACK!
        backgroundComponent.sprite.draw(_spriteBatch);
    }
}

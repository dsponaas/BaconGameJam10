package com.grizbenzis.bgj10.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.SortedIteratingSystem;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.grizbenzis.bgj10.components.PositionComponent;
import com.grizbenzis.bgj10.components.RenderComponent;
import com.grizbenzis.bgj10.components.SpriteComponent;

/**
 * Created by sponaas on 1/22/16.
 */
public class RenderSpriteSystem extends SortedIteratingSystem {

    private SpriteBatch _spriteBatch;

    private ComponentMapper<SpriteComponent> _spriteComponents = ComponentMapper.getFor(SpriteComponent.class);

    public RenderSpriteSystem(SpriteBatch spriteBatchInit, int priority) {
        super(Family.all(RenderComponent.class, SpriteComponent.class, PositionComponent.class).get(), new RenderSystemZComparator(), priority);
        _spriteBatch = spriteBatchInit;
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        Sprite sprite = _spriteComponents.get(entity).sprite;
        sprite.draw(_spriteBatch);
    }
}

package com.grizbenzis.bgj10.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.grizbenzis.bgj10.Constants;
import com.grizbenzis.bgj10.EntityManager;
import com.grizbenzis.bgj10.Time;
import com.grizbenzis.bgj10.components.BloodComponent;
import com.grizbenzis.bgj10.components.SpriteComponent;

/**
 * Created by sponaas on 1/23/16.
 */
public class BloodSystem extends IteratingSystem {

    private ComponentMapper<BloodComponent> _bloodComponents = ComponentMapper.getFor(BloodComponent.class);
    private SpriteBatch _spriteBatch;

    public BloodSystem(SpriteBatch spriteBatch, int priority) {
        super(Family.all(BloodComponent.class).get(), priority);
        _spriteBatch = spriteBatch;
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        BloodComponent bloodComponent = _bloodComponents.get(entity);
        bloodComponent.timer -= Time.time;

        if(bloodComponent.timer <= 0f) {
            EntityManager.getInstance().destroyEntity(entity);
        }
        else {
            Sprite sprite = bloodComponent.sprite;
            sprite.setAlpha(bloodComponent.timer / Constants.BLOOD_FADE_TIME);
            sprite.draw(_spriteBatch);
        }
    }
}

package com.grizbenzis.bgj10.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.grizbenzis.bgj10.Constants;
import com.grizbenzis.bgj10.EntityManager;
import com.grizbenzis.bgj10.ResourceManager;
import com.grizbenzis.bgj10.Time;
import com.grizbenzis.bgj10.components.*;

/**
 * Created by sponaas on 1/22/16.
 */
public class PlayerDataSystem extends IteratingSystem {

    private ComponentMapper<PlayerDataComponent> _playerDataComponents = ComponentMapper.getFor(PlayerDataComponent.class);
    private ComponentMapper<RenderComponent> _renderComponents = ComponentMapper.getFor(RenderComponent.class);
    private ComponentMapper<PositionComponent> _positionComponents = ComponentMapper.getFor(PositionComponent.class);
    private ComponentMapper<SpriteComponent> _spriteComponents = ComponentMapper.getFor(SpriteComponent.class);
    private ComponentMapper<BodyComponent> _bodyComponents = ComponentMapper.getFor(BodyComponent.class);

    public PlayerDataSystem(int priority) {
        super(Family.all(PlayerDataComponent.class).get(), priority);
    }

    private final float INVINCIBLE_BLINK_HACK = 10f;
    private static float bloodTimerHack = -1f;

    private static final Vector2 BLOOD_POS_OFFSET = new Vector2(0f, 0f);
    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        PlayerDataComponent playerDataComponent = _playerDataComponents.get(entity);
        RenderComponent renderComponent = _renderComponents.get(entity);
        PositionComponent positionComponent = _positionComponents.get(entity);
        SpriteComponent spriteComponent = _spriteComponents.get(entity);
        BodyComponent bodyComponent = _bodyComponents.get(entity);

        if (playerDataComponent.explosionUpTime >= 0f) {
            playerDataComponent.explosionUpTime -= (float) Time.time;
        }
        if (playerDataComponent.spreadShotTime >= 0f) {
            playerDataComponent.spreadShotTime -= (float) Time.time;
        }
        if (playerDataComponent.points2xTime >= 0f) {
            playerDataComponent.points2xTime -= (float) Time.time;
        }

        if (playerDataComponent.invincibilityTime > 0f) {
            boolean visible = true;
            float hackVal = playerDataComponent.invincibilityTime;
            while (hackVal > 0f) {
                hackVal -= INVINCIBLE_BLINK_HACK;
                visible = !visible;
            }

            if (visible && (null == renderComponent))
                entity.add(new RenderComponent(0));
            else if (!visible && (null != renderComponent))
                entity.remove(RenderComponent.class);

            playerDataComponent.invincibilityTime -= (float) Time.time;
        } else if ((null == renderComponent) && (playerDataComponent.alive)) {
            entity.add(new RenderComponent(0));
        } //else if(!playerDataComponent.alive) {
//            bloodTimerHack -= Time.time;
//            if(bloodTimerHack < 0f) {
//                bloodTimerHack = Constants.BLEED_INTERVAL;
//
//                Fixture uglyFixtureHackFixture = null;
//                for(Fixture curFixture : bodyComponent.body.getFixtureList()) {
//                    if(curFixture.isSensor()) {
//                        uglyFixtureHackFixture = curFixture;
//                        Rectangle fixtureRect = (Rectangle)uglyFixtureHackFixture.getShape();
//                    }
//                }
//                Vector2 bloodPos = new Vector2(uglyFixtureHackFixture.getShape().);
//
//                Entity bloodEntity = new Entity();
//                BloodComponent bloodComponent = new BloodComponent(new Sprite(ResourceManager.getTexture("blood1")));
//
//                float rotation = positionComponent.rotation;
//                rotation += Constants.ONE_AND_A_HALF_PI;
//                if(rotation > Constants.TWO_PI) {
//                    rotation -= Constants.TWO_PI;
//                }
//                else if(rotation < 0f) {
//                    rotation += Constants.TWO_PI;
//                }
//                Vector2 bloodPositionOffsetHack = new Vector2(BLOOD_POS_OFFSET).rotateRad(rotation);
//                bloodPos = bloodPos.add(bloodPositionOffsetHack).add(bloodPositionOffsetHack);
//
//                bloodComponent.sprite.setX(bloodPos.x);
//                bloodComponent.sprite.setY(bloodPos.y);
//                bloodEntity.add(bloodComponent);
//                EntityManager.getInstance().addEntity(bloodEntity);
//            }
//        }
    }

}
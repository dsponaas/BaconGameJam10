package com.grizbenzis.bgj10.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.grizbenzis.bgj10.Constants;
import com.grizbenzis.bgj10.EntityManager;
import com.grizbenzis.bgj10.GameState;
import com.grizbenzis.bgj10.Time;
import com.grizbenzis.bgj10.components.PlayerDataComponent;
import com.grizbenzis.bgj10.components.PowerupComponent;

/**
 * Created by sponaas on 1/22/16.
 */
public class PowerupSystem extends IteratingSystem {

    private ComponentMapper<PowerupComponent> _powerupComponents = ComponentMapper.getFor(PowerupComponent.class);
    private ComponentMapper<PlayerDataComponent> _playerDataComponents = ComponentMapper.getFor(PlayerDataComponent.class);

    public PowerupSystem(int priority) {
        super(Family.all(PowerupComponent.class).get(), priority);
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        PowerupComponent powerupComponent = _powerupComponents.get(entity);
        if(powerupComponent.pickedUp) {
            EntityManager.getInstance().destroyEntity(entity);
            PlayerDataComponent playerDataComponent = _playerDataComponents.get(GameState.getInstance().getPlayer().getEntity());
            if(Constants.PowerupType.POINTS_2X.ordinal() == powerupComponent.type) {
                playerDataComponent.points2xTime = Constants.POWERUP_TIMER;
            }
            else if(Constants.PowerupType.SPRD_SHOT.ordinal() == powerupComponent.type) {
                playerDataComponent.spreadShotTime = Constants.POWERUP_TIMER;
            }
            else if(Constants.PowerupType.RPD_SHOT.ordinal() == powerupComponent.type) {
                playerDataComponent.rapidShotTime = Constants.POWERUP_TIMER;
            }
            else if(Constants.PowerupType.EXTRA_LIFE.ordinal() == powerupComponent.type) {
                GameState.getInstance().incrementLives();
            }
        }

        powerupComponent.timer -= (float) Time.time;
        if(powerupComponent.timer < 0f) {
            EntityManager.getInstance().destroyEntity(entity);
        }
    }

}

package com.grizbenzis.bgj10.actors;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.grizbenzis.bgj10.BodyFactory;
import com.grizbenzis.bgj10.GameState;
import com.grizbenzis.bgj10.ResourceManager;
import com.grizbenzis.bgj10.components.*;

/**
 * Created by sponaas on 1/22/16.
 */
public class Player extends Actor {

    public Player(Entity entity) {
        super(entity);
    }

    public static Player makePlayer() {
        Entity entity = new Entity();

        Sprite sprite = new Sprite(ResourceManager.getTexture("player"));
        SpriteComponent spriteComponent = new SpriteComponent(sprite);
        Vector2 position = new Vector2(GameState.getInstance().getWidth() / 2, GameState.getInstance().getHeight() / 2);

        Body body = BodyFactory.getInstance().generate(entity, "player.json", position);

        PositionComponent playerPositionComponent = new PositionComponent(position.x, position.y);
        BodyComponent playerBodyComponent = new BodyComponent(playerPositionComponent, body);
        RenderComponent renderComponent = new RenderComponent(0);
        PlayerDataComponent playerDataComponent = new PlayerDataComponent();

        entity.add(playerPositionComponent).add(playerBodyComponent).add(spriteComponent).add(renderComponent).add(playerDataComponent);

        Player player = new Player(entity);
        player.setSizeXInPixels(sprite.getWidth());
        player.setSizeYInPixels(sprite.getHeight());

        return player;
    }
}

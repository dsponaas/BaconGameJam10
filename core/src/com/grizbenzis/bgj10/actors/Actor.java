package com.grizbenzis.bgj10.actors;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.physics.box2d.Body;
import com.grizbenzis.bgj10.components.BodyComponent;
import com.grizbenzis.bgj10.components.PositionComponent;

/**
 * Created by sponaas on 1/22/16.
 */
public class Actor {

    private Entity _entity;
    public Entity getEntity()           { return _entity; }

    private Body _body;
    protected Body getBody()            { return _body; }

    private float _sizeXInPixels;
    protected void setSizeXInPixels(float size)     { _sizeXInPixels = size; }
    protected float getSizeXInPixels()              { return _sizeXInPixels; }

    private float _sizeYInPixels;
    protected void setSizeYInPixels(float size)     { _sizeYInPixels = size; }
    protected float getSizeYInPixels()              { return _sizeYInPixels; }

    private PositionComponent _positionComponent;
    protected PositionComponent getPosition()     { return _positionComponent; }

    public Actor(Entity entity) {
        _entity = entity;
        _body = _entity.getComponent(BodyComponent.class).body;
        _positionComponent = _entity.getComponent(PositionComponent.class);
    }

    public void update() {}

}

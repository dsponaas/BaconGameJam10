package com.grizbenzis.bgj10.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by sponaas on 1/22/16.
 */
public class PositionComponent extends Component {

    public float x, y, rotation;

    public PositionComponent(Vector2 init) {
        this(init.x, init.y);
    }

    public PositionComponent(float xInit, float yInit) {
        x= xInit;
        y = yInit;
        rotation = 0f;
    }

}

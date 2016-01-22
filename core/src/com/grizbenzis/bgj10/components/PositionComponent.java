package com.grizbenzis.bgj10.components;

import com.badlogic.ashley.core.Component;

/**
 * Created by sponaas on 1/22/16.
 */
public class PositionComponent extends Component {

    public float x, y;

    public PositionComponent(float xInit, float yInit) {
        x= xInit;
        y = yInit;
    }

}

package com.grizbenzis.bgj10.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.physics.box2d.Body;
import com.grizbenzis.bgj10.Constants;

/**
 * Created by sponaas on 1/22/16.
 */
public class BodyComponent extends Component {

    public Body body;

    public BodyComponent(PositionComponent positionComponent, Body bodyInit) {
        body = bodyInit;
        body.setTransform( positionComponent.x * Constants.PIXELS_TO_METERS, positionComponent.y * Constants.PIXELS_TO_METERS, positionComponent.rotation);
    }

}

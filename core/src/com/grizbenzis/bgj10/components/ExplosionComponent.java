package com.grizbenzis.bgj10.components;

import com.badlogic.ashley.core.Component;

/**
 * Created by kyle on 1/23/16.
 */
public class ExplosionComponent extends Component {

    public float timeLeft;

    public ExplosionComponent(float timeLeftInit) {
        timeLeft = timeLeftInit;
    }

}

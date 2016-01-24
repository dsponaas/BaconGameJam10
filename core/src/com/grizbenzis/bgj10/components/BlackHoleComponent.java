package com.grizbenzis.bgj10.components;

import com.badlogic.ashley.core.Component;
import com.grizbenzis.bgj10.Constants;

/**
 * Created by sponaas on 1/24/16.
 */
public class BlackHoleComponent extends Component {

    public float lifeTimer;

    public BlackHoleComponent() {
        lifeTimer = Constants.BLACK_HOLE_LIFE_TIME;
    }

}

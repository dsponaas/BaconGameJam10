package com.grizbenzis.bgj10.components;

import com.badlogic.ashley.core.Component;
import com.grizbenzis.bgj10.Constants;

/**
 * Created by sponaas on 1/22/16.
 */
public class PowerupComponent extends Component {

    public int type;
    public float timer;
    public boolean pickedUp;

    public PowerupComponent(Constants.PowerupType typeInit) {
        type = typeInit.ordinal();
        pickedUp = false;
        timer = Constants.POWERUP_TIMER;
    }

}

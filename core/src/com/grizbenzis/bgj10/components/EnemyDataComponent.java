package com.grizbenzis.bgj10.components;

import com.badlogic.ashley.core.Component;
import com.grizbenzis.bgj10.Constants;

/**
 * Created by sponaas on 1/22/16.
 */
public class EnemyDataComponent extends Component {

    public Constants.EnemyType type;
    public boolean alive;

    public EnemyDataComponent(Constants.EnemyType init) {
        type = init;
        alive = true;
    }

    public int getPoints() {
        switch(type) {
            case ASTEROID_LARGE: return 10;
            case ASTEROID_MEDIUM: return 15;
            case ASTEROID_SMALL: return 20;
        }
        return 0;
    }
}

package com.grizbenzis.bgj10.components;

import com.badlogic.ashley.core.Component;

/**
 * Created by sponaas on 1/22/16.
 */
public class EnemyDataComponent extends Component {

    public int points;

    public EnemyDataComponent(int pointsInit) {
        points = pointsInit;
    }

}

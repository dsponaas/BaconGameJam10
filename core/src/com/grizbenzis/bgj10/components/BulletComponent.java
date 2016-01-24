package com.grizbenzis.bgj10.components;

import com.badlogic.ashley.core.Component;

/**
 * Created by sponaas on 6/13/15.
 */
public class BulletComponent extends Component {

    public boolean detonate;
    public boolean largeExplosion;

    public BulletComponent() {
        detonate = false;
        largeExplosion = false;
    }

}

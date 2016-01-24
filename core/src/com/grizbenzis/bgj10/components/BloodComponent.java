package com.grizbenzis.bgj10.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.grizbenzis.bgj10.Constants;

/**
 * Created by sponaas on 1/23/16.
 */
public class BloodComponent extends Component {

    public Sprite sprite;
    public float timer;

    public BloodComponent(Sprite spriteInit) {
        sprite = spriteInit;
        timer = Constants.BLOOD_FADE_TIME;
    }

}

package com.grizbenzis.bgj10.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.g2d.Sprite;

/**
 * Created by sponaas on 1/23/16.
 */
public class ParallaxBackgroundComponent extends Component {

    public Sprite sprite;
    public int renderOrder;
    public float positionX;
    public float positionY;
    public float scrollSpeed;

    public ParallaxBackgroundComponent(Sprite spriteInit, int renderOrderInit, float scrollSpeedInit, float positionYInit) {
        sprite = spriteInit;
        renderOrder = renderOrderInit;
        scrollSpeed = scrollSpeedInit;
        positionX = 0f;
        positionY = positionYInit;
    }

}

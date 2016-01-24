package com.grizbenzis.bgj10.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.g2d.Sprite;

/**
 * Created by sponaas on 1/22/16.
 */
public class SpriteComponent extends Component {

    public Sprite sprite;
    public float spriteWidth, spriteHeight;

    public SpriteComponent(Sprite spriteInit) {
        sprite = spriteInit;
        spriteWidth = sprite.getWidth();
        spriteHeight = sprite.getHeight();
    }

}

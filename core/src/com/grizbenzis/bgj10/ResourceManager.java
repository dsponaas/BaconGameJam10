package com.grizbenzis.bgj10;

import com.badlogic.gdx.graphics.Texture;

import java.util.HashMap;

/**
 * Created by sponaas on 1/22/16.
 */
public class ResourceManager {

    private static HashMap<String, Texture> _textures;

    public static void initialize() {
        _textures = new HashMap<String, Texture>();

        _textures.put("splashscreen", new Texture("splashscreen.png"));
    }

    public static Texture getTexture(String name) {
        return _textures.get(name);
    }
}

package com.grizbenzis.bgj10;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;

import java.util.HashMap;

/**
 * Created by sponaas on 1/22/16.
 */
public class ResourceManager {

    private static HashMap<String, Texture> _textures;

    private static BitmapFont _hudFont;
    private static BitmapFont _scoreFont;

    public static void initialize() {
        _textures = new HashMap<String, Texture>();

        _textures.put("splashscreen", new Texture("splashscreen.png"));
        _textures.put("background", new Texture("background.png"));
        _textures.put("player", new Texture("player.png"));

        initFonts();
    }

    private static void initFonts() {
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("OpenSans-Regular.ttf"));

        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 24;
        _hudFont = generator.generateFont(parameter);

        FreeTypeFontGenerator.FreeTypeFontParameter parameter2 = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter2.size = 64;
        _scoreFont = generator.generateFont(parameter2);

        generator.dispose();
    }
    public static Texture getTexture(String name) {
        return _textures.get(name);
    }
    public static BitmapFont getHudFont() { return _hudFont; }
    public static BitmapFont getScoreFont() { return _scoreFont; }


}

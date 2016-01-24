package com.grizbenzis.bgj10;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
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

    private static Music _gameMusic;
    private static Sound _playerShootingSound;
    private static Sound _playerDeathSound;
    private static Sound _explosionSound;

    public static void initialize() {
        _textures = new HashMap<String, Texture>();

        _textures.put("splashscreen", new Texture("splashscreen.png"));
        _textures.put("background", new Texture("background.png"));
        _textures.put("player", new Texture("player.png"));
        _textures.put("player_dead", new Texture("player_dead.png"));
        _textures.put("powerup_rpd", new Texture("powerup_rpd.png"));
        _textures.put("powerup_sprd", new Texture("powerup_sprd.png"));
        _textures.put("powerup_2x", new Texture("powerup_2x.png"));
        _textures.put("powerup_1up", new Texture("powerup_1up.png"));
        _textures.put("powerup_expl", new Texture("powerup_expl.png"));
        _textures.put("powerup_sprd_small", new Texture("powerup_sprd_small.png"));
        _textures.put("powerup_rpd_small", new Texture("powerup_rpd_small.png"));
        _textures.put("powerup_2x_small", new Texture("powerup_2x_small.png"));
        _textures.put("powerup_expl_small", new Texture("powerup_expl_small.png"));
        _textures.put("bullet", new Texture("bullet.png"));
        _textures.put("asteroid_large", new Texture("asteroid_large.png"));
        _textures.put("asteroid_medium", new Texture("asteroid_medium.png"));
        _textures.put("asteroid_small", new Texture("asteroid_small.png"));
        _textures.put("alien", new Texture("alien.png"));
        _textures.put("gameover", new Texture("gameover.png"));
        _textures.put("explosion", new Texture("explosion.png"));
        _textures.put("explosion_large", new Texture("explosion_large.png"));
        _textures.put("background_rear", new Texture("background_rear.png"));
        _textures.put("background_middle", new Texture("background_middle.png"));
        _textures.put("background_front", new Texture("background_front.png"));
        _textures.put("blood1", new Texture("blood1.png"));
        _textures.put("black_hole", new Texture("black_hole.png"));

        _gameMusic = Gdx.audio.newMusic(Gdx.files.internal("test4_looping.ogg"));
        _gameMusic.setLooping( true );

        _playerShootingSound = Gdx.audio.newSound(Gdx.files.internal("playershooting.ogg"));
        _explosionSound = Gdx.audio.newSound(Gdx.files.internal("explosion.ogg"));
        _playerDeathSound = Gdx.audio.newSound(Gdx.files.internal("death.ogg"));

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
    public static Music getGameMusic() {
        return _gameMusic;
    }
    public static Sound getPlayerShootingSound() {
        return _playerShootingSound;
    }
    public static Sound getExplosionSound() {
        return _explosionSound;
    }
    public static Sound getPlayerDeathSound() {
        return _playerDeathSound;
    }


}

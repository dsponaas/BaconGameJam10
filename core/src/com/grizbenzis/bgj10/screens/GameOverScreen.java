package com.grizbenzis.bgj10.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.grizbenzis.bgj10.GameState;
import com.grizbenzis.bgj10.ResourceManager;
import com.grizbenzis.bgj10.bgj10;

import static com.grizbenzis.bgj10.ResourceManager.*;

/**
 * Created by sponaas on 1/22/16.
 */
public class GameOverScreen implements Screen {

    private SpriteBatch _spriteBatch;
    private SpriteBatch _hudBatch;
    private Sprite _titleSprite;

    private ImageButton _startButton;

    public GameOverScreen() {}

    @Override
    public void show() {
        _spriteBatch = new SpriteBatch();
        _hudBatch = new SpriteBatch();

        _titleSprite = new Sprite(getTexture("gameover"));
        _titleSprite.setPosition(0f, 0f);

        InputMultiplexer inputMultiplexer = new InputMultiplexer();
        inputMultiplexer.addProcessor(new SimpleInput());
        Gdx.input.setInputProcessor(inputMultiplexer);

        ResourceManager.getGameMusic().stop();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        _spriteBatch.begin();
        _titleSprite.draw(_spriteBatch);
        _spriteBatch.end();

        BitmapFont scoreFont = getScoreFont();

        _hudBatch.begin();
        int score = GameState.getInstance().getScore();

        float xMod = -28f;
        float scoreHackCounter = (float)score;
        while(scoreHackCounter > 1f) {
            scoreHackCounter /= 10f;
            xMod -= 15f;
        }

        scoreFont.draw(_hudBatch, "" + score, (Gdx.graphics.getWidth() / 2) + xMod, (Gdx.graphics.getHeight() / 2) - 16);
        _hudBatch.end();
    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        _spriteBatch.dispose();
        _hudBatch.dispose();
    }

    public class SimpleInput implements InputProcessor {

        @Override
        public boolean keyDown(int keycode) {
            bgj10.game.setScreen(new GameScreen());
            return true;
        }

        @Override
        public boolean keyUp(int keycode) {
            return false;
        }

        @Override
        public boolean keyTyped(char character) {
            return false;
        }

        @Override
        public boolean touchDown(int screenX, int screenY, int pointer, int button) {
            return false;
        }

        @Override
        public boolean touchUp(int screenX, int screenY, int pointer, int button) {
            return false;
        }

        @Override
        public boolean touchDragged(int screenX, int screenY, int pointer) {
            return false;
        }

        @Override
        public boolean mouseMoved(int screenX, int screenY) {
            return false;
        }

        @Override
        public boolean scrolled(int amount) {
            return false;
        }
    }

}

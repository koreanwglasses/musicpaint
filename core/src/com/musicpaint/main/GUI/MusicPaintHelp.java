package com.musicpaint.main.GUI;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.GL30;

/**
 * Created by fchoi on 5/26/2016.
 */
public class MusicPaintHelp implements Screen {
    Game game;
    Screen prev;

    HorizontalSlidingPane hsp;

    public MusicPaintHelp(Game game, Screen prev) {
        this.game = game;
        this.prev = prev;
    }

    @Override
    public void show() {
        hsp = new HorizontalSlidingPane();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(Color.ORANGE.r, Color.ORANGE.g, Color.ORANGE.b, Color.ORANGE.a);
        Gdx.gl.glClear(GL30.GL_COLOR_BUFFER_BIT);
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

    }

    public void exit() {
        game.setScreen(prev);
    }
}

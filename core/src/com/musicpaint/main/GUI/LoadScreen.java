package com.musicpaint.main.GUI;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.ScreenUtils;

/**
 * Created by fchoi on 3/1/2016.
 */
public class LoadScreen implements Screen {

    Game game;

    ShapeRenderer sr;
    SpriteBatch sb;
    OrthographicCamera camera;

    Texture logo;

    float accum;

    public LoadScreen(Game game) {
        this.game = game;
    }

    @Override
    public void show() {
        Assets.load();

//        sr = new ShapeRenderer();
        sb = new SpriteBatch();
        camera = new OrthographicCamera();

        logo = new Texture(Gdx.files.internal("GUI/logo.png"));

        accum = 0;
    }

    @Override
    public void render(float delta) {
        if(Assets.manager.update() && accum > 1f) {
            Assets.initialize();
            PainterUIMin ui = new PainterUIMin(game);
            ui.fade_in(ScreenUtils.getFrameBufferTexture());
            game.setScreen(ui);
        }

        Gdx.gl.glClearColor(1f, 1f, 1f, 1);
        Gdx.gl.glClear(GL30.GL_COLOR_BUFFER_BIT);

//        sr.begin(ShapeRenderer.ShapeType.Filled);
//        sr.setColor(Color.LIGHT_GRAY);
//        sr.rect(-30, -15, 60, 30);
//        sr.setColor(Color.GRAY);
//        sr.rect(-30, -15, 60, Assets.manager.getProgress() * 30);
//        sr.end();

        sb.begin();
        sb.draw(logo, -30 ,-15, 60, 30, 0, 1, 1, 0);
        sb.end();

        accum += delta;
    }

    @Override
    public void resize(int width, int height) {
        if(width > height) {
            camera.viewportWidth = 100;
            camera.viewportHeight = 100f * height / width;
        } else {
            camera.viewportWidth = 100f *  width / height;
            camera.viewportHeight = 100;
        }

        camera.update();
//        sr.setProjectionMatrix(camera.combined);
        sb.setProjectionMatrix(camera.combined);
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
}

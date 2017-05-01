package com.musicpaint.main.GUI;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.musicpaint.main.Math2;

/**
 * Created by fchoi on 6/7/2016.
 */
public abstract class Screen2 implements Screen {
    Game game;

    boolean flashing;
    Color flashColor;

    boolean fading_in;
    float fade_in_timer;
    TextureRegion buff_tex;

    Camera camera;

    ShapeRenderer sr;
    SpriteBatch sb;

    public Screen2(Game game) {
        this.game = game;

        sr = new ShapeRenderer();

        Camera sr_camera = new OrthographicCamera();
        sr_camera.viewportWidth = 100;
        sr_camera.viewportHeight = 100;
        sr_camera.update();
        sr.setProjectionMatrix(sr_camera.combined);

        sb = new SpriteBatch();
        camera = new OrthographicCamera();
    }

    public void flash(Color color){
        flashing = true;
        flashColor = color.cpy();
    }

    public void fade_in(TextureRegion buff) {
        fading_in = true;
        fade_in_timer = 0;
        buff_tex = buff;
    }

    @Override
    public void render(float delta) {
        base_render(delta);
        overlay_render(delta);
    }

    public abstract void base_render(float delta);

    public void overlay_render(float delta) {
        if(fading_in) {
            float alpha = 1 - Math2.easeInOutPow(fade_in_timer / 1.5f, 2.3f);
            if(fade_in_timer >= 1.5)
                fading_in = false;
            else {
                Gdx.gl.glEnable(GL20.GL_BLEND);
                Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

                sb.begin();
                sb.setColor(1, 1, 1, alpha);
                sb.draw(buff_tex, 0, 0);
                sb.end();

                Gdx.gl.glDisable(GL20.GL_BLEND);
            }
            fade_in_timer += delta;
        }
        if(flashing) {
            if(flashColor.a <= 0)
                flashing = false;
            else {
                Gdx.gl.glEnable(GL20.GL_BLEND);
                Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

                sr.begin(ShapeRenderer.ShapeType.Filled);
                sr.setColor(flashColor);
                sr.rect(-50,-50,100,100);
                sr.end();

                Gdx.gl.glDisable(GL20.GL_BLEND);
            }
            flashColor.a -= delta / 1;
        }
    }

    @Override
    public void resize(int width, int height) {
        camera.viewportWidth = width;
        camera.viewportHeight = height;
        camera.position.x = width / 2f;
        camera.position.y = height / 2f;
        camera.update();

        sb.setProjectionMatrix(camera.combined);
    }
}

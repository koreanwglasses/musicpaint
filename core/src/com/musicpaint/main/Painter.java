package com.musicpaint.main;

import com.badlogic.gdx.*;
import com.badlogic.gdx.audio.AudioRecorder;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.musicpaint.main.GUI.PainterUIMin;
import com.musicpaint.main.SignalColor.SignalColor;
import com.musicpaint.main.SignalColor.SignalColorScheme;

/**
 * Created by fchoi on 2/18/2016.
 */
public class Painter implements Screen{
    SpriteBatch spriteBatch;
    Voronoi voronoi;

    public OrthographicCamera getCamera() {
        return camera;
    }

    OrthographicCamera camera;

    public SignalColor signalColor;
    WaterColor waterColor;
    AudioRecorder recorder;
    short[] buffer;

    boolean isPaused;

    boolean slowMode;

    public Color getBackground() {
        return background;
    }

    public void setBackground(Color background) {
        this.background = background;
        voronoi.setBackground(background);
    }

    Color background;

    int stepsPerDraw = 2;

    int width = 300;
    int height = 300;

    int size = 1024;

    float viewportWidth;
    float viewportHeight;

    public float getScale() {
        return scale;
    }

    float scale = 1;

    public float getTargetScale() {
        return targetScale;
    }

    float targetScale = scale;

    double currentAmplitude;

    public Color getCurrentColor() {
        return currentColor;
    }

    Color currentColor;

    public Painter() {
        signalColor = new SignalColor(size, .05, .5, 44100);
        waterColor = new WaterColor(width, height);
        recorder = Gdx.audio.newAudioRecorder(44100, true);
        buffer = new short[size * stepsPerDraw];

        isPaused = true;
    }

    @Override
    public void show() {
        spriteBatch = new SpriteBatch();
        voronoi = new Voronoi(background, width, height);
        voronoi.initialize();
        camera = new OrthographicCamera();

        slowMode = false;
    }

    public double getCurrentAmplitude() {
        return currentAmplitude;
    }

    @Override
    public void render(float delta) {
        if (!isPaused) {
            recorder.read(buffer, 0, buffer.length);

            currentAmplitude = SignalColor.amplitude(buffer, 0, size);
            currentColor = signalColor.SampleToColor(buffer, 0);

            if (currentColor.a > 0)
                waterColor.newPixel(currentColor);

            for (int i = 1; i < stepsPerDraw; i++) {
                Color pixel = signalColor.SampleToColor(buffer, i * size);
                if (pixel.a > 0)
                    waterColor.newPixel(pixel);
            }
        }

        updateCamera(delta);

        Gdx.gl.glClearColor(background.r, background.g, background.b, background.a);
        Gdx.gl.glClear(GL30.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        if (slowMode) {
            Pixmap pix = waterColor.makePixmap(background);
            Texture canvas = new Texture(pix, true);
            pix.dispose();

            spriteBatch.begin();
            spriteBatch.draw(canvas, -width / 2, -height / 2);
            spriteBatch.end();
        } else
            voronoi.render(waterColor.getBitmap(), waterColor.getEdge());
    }

    @Override
    public void resize(int width, int height) {
        if (width * this.height > this.width * height) {
            viewportWidth = this.width;
            viewportHeight = (float) height / width * this.height;
        } else {
            viewportWidth = (float) width / height * this.width;
            viewportHeight = this.height;
        }

        updateCamera(0);
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
        recorder.dispose();
    }

    public void reset() {
        waterColor.dispose();
        waterColor = new WaterColor(width, height);
//        camera.position.x = 0;
//        camera.position.y = 0;
//        setScale(1);

        slowMode = false;
    }

    public void stop() {
        isPaused = true;
    }

    public void start() {
        isPaused = false;
    }

    public void setScale(float scale) {
        targetScale = scale;
    }

    public void updateCamera(float delta) {
        // scale = lerp(scale, targetScale, delta * 10);
        scale = targetScale;

        camera.viewportWidth = viewportWidth * scale;
        camera.viewportHeight = viewportHeight * scale;
        camera.update();

        spriteBatch.setProjectionMatrix(camera.combined);
        voronoi.setProjectionMatrix(camera.combined);
    }

    public void setColorScheme(SignalColorScheme colorScheme) {
        signalColor.setColorScheme(colorScheme);
        setBackground(colorScheme.getBackgroundColor());
    }

    public SignalColorScheme getColorScheme() {
        return signalColor.getColorScheme();
    }

    public void setSlowMode(boolean isSlowMode) {
        this.slowMode = isSlowMode;
    }

    public boolean isPaused() {
        return isPaused;
    }
}

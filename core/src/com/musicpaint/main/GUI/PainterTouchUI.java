package com.musicpaint.main.GUI;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.musicpaint.main.Painter;

/**
 * Created by fchoi on 2/19/2016.
 */
public class PainterTouchUI implements GestureDetector.GestureListener {

    Painter parent;

    float initialTargetScale;

    public PainterTouchUI(Painter parent) {
        this.parent = parent;

        initialTargetScale = parent.getScale();
    }

    @Override
    public boolean touchDown(float x, float y, int pointer, int button) {
        initialTargetScale = parent.getTargetScale();
        return true;
    }

    @Override
    public boolean tap(float x, float y, int count, int button) {
        return false;
    }

    @Override
    public boolean longPress(float x, float y) {
        return false;
    }

    @Override
    public boolean fling(float velocityX, float velocityY, int button) {
        return false;
    }

    @Override
    public boolean pan(float x, float y, float deltaX, float deltaY) {
        Camera camera = parent.getCamera();
        Vector3 init = camera.unproject(new Vector3(x, y, 0));
        Vector3 finl = camera.unproject(new Vector3(x + deltaX, y + deltaY, 0));

        camera.position.x -= finl.x - init.x;
        camera.position.y -= finl.y - init.y;

        return true;
    }

    @Override
    public boolean panStop(float x, float y, int pointer, int button) {
        return false;
    }

    @Override
    public boolean zoom(float initialDistance, float distance) {
        parent.setScale(initialTargetScale * initialDistance / distance);
        return true;
    }

    @Override
    public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2) {
        return false;
    }
}

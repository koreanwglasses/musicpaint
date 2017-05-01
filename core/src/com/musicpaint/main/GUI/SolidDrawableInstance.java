package com.musicpaint.main.GUI;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.utils.BaseDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

/**
 * Created by fchoi on 2/26/2016.
 */
public class SolidDrawableInstance extends BaseDrawable {

    SolidDrawable parent;

    public SolidDrawableInstance(SolidDrawable parent) {
        super();
        this.parent = parent;
    }

    @Override
    public void draw (Batch batch, float x, float y, float width, float height) {
        batch.setColor(parent.color);
        batch.draw(SolidDrawable.white, x, y, width, height);
    }
}

package com.musicpaint.main.GUI;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;

/**
 * Created by fchoi on 2/26/2016.
 */
public class SolidDrawable {
    public static Texture white;

    public Color color;

    public SolidDrawable () {

    }

    public SolidDrawableInstance getInstance() {
        initialize();
        return new SolidDrawableInstance(this);
    }

    private static boolean isInitialized = false;
    public static void initialize() {
        if(!isInitialized) {
            Pixmap whitePixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
            whitePixmap.setColor(Color.WHITE);
            whitePixmap.fill();
            white = new Texture(whitePixmap);
            whitePixmap.dispose();

            isInitialized = true;
        }
    }
}

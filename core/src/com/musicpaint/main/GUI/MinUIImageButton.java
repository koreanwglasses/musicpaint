package com.musicpaint.main.GUI;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

/**
 * Created by fchoi on 2/26/2016.
 */
public class MinUIImageButton extends ImageButton {

    static SolidDrawable hoverBackground;
    static SolidDrawable clickedBackground;

    public MinUIImageButton(Drawable image) {
        super(image);

        initialize();

        applyStyle();
    }

    public MinUIImageButton(ImageButtonStyle style) {
        super(style);

        initialize();

        applyStyle();
    }

    private void applyStyle() {
        ImageButtonStyle style = getStyle();
        style.over = hoverBackground.getInstance();
        style.down = clickedBackground.getInstance();
        setStyle(style);
    }

    private static boolean isInitialized = false;
    public static void initialize() {
        if(!isInitialized) {
            hoverBackground = new SolidDrawable();
            hoverBackground.color = new Color(0, 0, 0, .2f);
            clickedBackground = new SolidDrawable();
            clickedBackground.color = new Color(0, 0, 0, .5f);

            isInitialized = true;
        }
    }

    public void setImage(Drawable image) {
        ImageButtonStyle style = getStyle();
        style.imageDown = image;
        style.imageUp = image;
        style.imageChecked = image;
        style.imageCheckedOver = image;
        style.imageDisabled = image;
        style.imageOver = image;
        setStyle(style);
    }
}

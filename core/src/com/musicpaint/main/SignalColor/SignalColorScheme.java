package com.musicpaint.main.SignalColor;

import com.badlogic.gdx.graphics.Color;

/**
 * Created by fchoi on 3/1/2016.
 */
public abstract class SignalColorScheme {
    SignalColor parent;

    final String name;
    final Color backgroundColor;

    public SignalColorScheme(Color backgroundColor, String name) {
        this.backgroundColor = backgroundColor;
        this.name = name;
    }

    public void setParent(SignalColor parent) {
        this.parent = parent;
    }

    public Color getBackgroundColor() {
        return backgroundColor;
    }

    public String getName() {
        return name;
    }

    public abstract Color sampleToColor(double[] bins, double amplitude);
}

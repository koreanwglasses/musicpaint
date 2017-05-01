package com.musicpaint.main;

/**
 * Created by fchoi on 6/7/2016.
 */
public class Math2 {
    public static float easeInQuad(float t) {
        return t * t;
    }

    public static float easeInOutQuad(float t) {
        if(t < .5)
            return 2 * t * t;
        else
            return -2 * (t - 1) * (t - 1) + 1;
    }

    public static float easeInOutPow(float t, float p) {
        if(t < .5)
            return (float) (Math.pow(t, p) / Math.pow(2, 1 - p));
        else
            return (float) (1 - Math.pow(1 - t, p) / Math.pow(2, 1 - p));
    }
}

package com.musicpaint.main.SignalColor;

/**
 * Created by fchoi on 3/1/2016.
 */
public class ColorSchemeHelper {
    public static float[] HSVtoRGB(float h, float s, float v) {
        int i;
        float f, p, q, t, r, g, b;
        if (s == 0) {
            // achromatic (grey)
            r = g = b = v;
            return new float[]{r, g, b};
        }
        h /= 60;            // sector 0 to 5
        i = (int) Math.floor(h);
        f = h - i;          // factorial part of h
        p = v * (1 - s);
        q = v * (1 - s * f);
        t = v * (1 - s * (1 - f));
        switch (i) {
            case 0:
                r = v;
                g = t;
                b = p;
                break;
            case 1:
                r = q;
                g = v;
                b = p;
                break;
            case 2:
                r = p;
                g = v;
                b = t;
                break;
            case 3:
                r = p;
                g = q;
                b = v;
                break;
            case 4:
                r = t;
                g = p;
                b = v;
                break;
            default:        // case 5:
                r = v;
                g = p;
                b = q;
                break;
        }
        return new float[]{r, g, b};
    }

    public static float dbFlow(double amplitude, double level, double threshold, double minimumFlow) {
        double flow = -Math.log(amplitude / level) / Math.log(2 * threshold) + 1;
        flow = flow * (1 - minimumFlow) + minimumFlow;
        if (flow > 1) flow = 1;
        if (flow < 0) flow = 0;

        return (float) flow;
    }
}

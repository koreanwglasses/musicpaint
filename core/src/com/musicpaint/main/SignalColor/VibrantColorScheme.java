package com.musicpaint.main.SignalColor;

import com.badlogic.gdx.graphics.Color;

/**
 * Created by fchoi on 3/1/2016.
 */
public class VibrantColorScheme extends SignalColorScheme {

    double minimumFlow = .5;

    public VibrantColorScheme() {
        super(Color.GRAY, "Vibrant");
    }

    @Override
    public Color sampleToColor(double[] bins, double amplitude) {
        double r, g, b, sum;
        r = g = b = sum = 0;

        for (int i = 0; i < parent.size / 2; i++) {
            double freq = parent.binToFreq(i);
            double cents = Math.log(freq) / Math.log(2);
            float h = (float) ((cents - Math.floor(cents)) * 360);
            float octaves = (float) ((Math.log(freq) - Math.log(130.813))/Math.log(130.813));
            double m = bins[i];

            float l = octaves / 8 + .5f;
            if(l > .75) l = .75f;
            if(l < .25) l = .25f;

            float ss = l < .5 ? l : 1 - l;
            float v = l + ss;
            float s = (2 * ss) / (l + ss);


            float[] tcolor = ColorSchemeHelper.HSVtoRGB(h, s, v);
            r += tcolor[0] * m * m * m;
            g += tcolor[1] * m * m * m;
            b += tcolor[2] * m * m * m;
            sum += m * m * m;
        }

        r /= sum;
        g /= sum;
        b /= sum;

        float flow = ColorSchemeHelper.dbFlow(amplitude, parent.getLevel(), parent.getThreshold(), minimumFlow);
        return new Color((float) r, (float) g, (float) b, flow);
    }
}

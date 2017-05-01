package com.musicpaint.main.SignalColor;

import com.badlogic.gdx.graphics.Color;

/**
 * Created by fchoi on 3/1/2016.
 */
public class PastelColorScheme extends SignalColorScheme {

    double minimumFlow = .5;

    public PastelColorScheme() {
        super(Color.WHITE, "Pastel");
    }

    @Override
    public Color sampleToColor(double[] bins, double amplitude) {
        double r, g, b;
        r = g = b = 0;

        for (int i = 0; i < parent.size / 2; i++) {
            double freq = parent.binToFreq(i);
            double cents = Math.log(freq) / Math.log(2);
            float h = (float) ((cents - Math.floor(cents)) * 360);
            float v = (float) (Math.log(freq) / Math.log(parent.binToFreq(parent.size / 2)));
            double m = bins[i];

            float[] tcolor = ColorSchemeHelper.HSVtoRGB(h, 1, 1);
            r += tcolor[0] * m;
            g += tcolor[1] * m;
            b += tcolor[2] * m;
        }

        double mag = Math.max(r, Math.max(g, b));

        r /= mag;
        g /= mag;
        b /= mag;

        float flow = ColorSchemeHelper.dbFlow(amplitude, parent.getLevel(), parent.getThreshold(), minimumFlow);
        return new Color((float) r, (float) g, (float) b, flow);
    }
}

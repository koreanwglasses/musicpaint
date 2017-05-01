package com.musicpaint.main.SignalColor;

import com.badlogic.gdx.graphics.Color;
import org.jtransforms.fft.DoubleFFT_1D;

/**
 * Created by fchoi on 2/18/2016.
 */
public class SignalColor {
    int size;

    public double getThreshold() {
        return threshold;
    }

    public void setThreshold(double threshold) {
        this.threshold = threshold;
    }

    double threshold;

    public double getLevel() {
        return level;
    }

    public void setLevel(double level) {
        this.level = level;
    }

    double level;
    int sampleRate;

    public SignalColorScheme colorScheme;

    private DoubleFFT_1D fft;

    public SignalColor(int size, double threshold, double level, int sampleRate) {
        this.size = size;
        this.threshold = threshold;
        this.level = level;
        this.sampleRate = sampleRate;

        fft = new DoubleFFT_1D(size);

        data = new double[size * 2];
    }

    public SignalColorScheme getColorScheme() {
        return colorScheme;
    }

    public void setColorScheme(SignalColorScheme colorScheme) {
        colorScheme.setParent(this);
        this.colorScheme = colorScheme;
    }

    static double hannWindow(int n, int frameSize) {
        return 0.5 * (1 - Math.cos((2 * Math.PI * n) / (frameSize - 1)));
    }

    double binToFreq(int bin) {
        return (bin + .5) * sampleRate / size;
    }

    private double[] data;
    public Color SampleToColor(short[] buffer, int offset) {
        double amplitude = amplitude(buffer, offset, size);
        if (amplitude < threshold)
            return new Color(0, 0, 0, 0);

        for (int i = 0; i < size; i++) {
            data[i * 2] = buffer[i + offset] * hannWindow(i, size) / 32768;
            data[i * 2 + 1] = 0.0;
        }

        fft.complexForward(data);

        double[] bins = new double[data.length / 4];
        for (int i = 0; i < data.length / 4; i++)
            bins[i] = Math.sqrt(data[i * 2] * data[i * 2] + data[i * 2 + 1] * data[i * 2 + 1]);

        return colorScheme.sampleToColor(bins, amplitude);
    }

    public static double amplitude(short[] buffer, int offset, int size) {
        double amplitude = 0;
        for (int i = offset; i < offset + size; i++)
            if (Math.abs((int) buffer[i]) > amplitude) amplitude = Math.abs((int) buffer[i]);
        amplitude /= 32768;
        return amplitude;
    }
}

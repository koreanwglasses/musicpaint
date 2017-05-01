package com.musicpaint.main;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import org.lwjgl.util.Point;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;

/**
 * Created by fchoi on 2/18/2016.
 */
public class WaterColor {

    public Color[][] getBitmap() {
        return _bitmap;
    }

    public int getHeight() {
        return _height;
    }

    public int getWidth() {
        return _width;
    }

    public HashSet<Point> getEdge () { return _available; }

    Color[][] _bitmap;
    int _height;
    int _width;

    HashSet<Point> _available;

    public WaterColor(int width, int height) {
        _available = new HashSet<Point>();
        _bitmap = new Color[width][height];
        _height = height;
        _width = width;
    }

    public void newPixel(Color color) {
        Point bestxy;

        if(_available.isEmpty()) bestxy = new Point(_width / 2, _height / 2);
        else bestxy = bestFit(color);

        _bitmap[bestxy.getX()][bestxy.getY()] = color;

        _available.remove(bestxy);
        for (Point nxy : getNeighbors(bestxy))
            if (_bitmap[nxy.getX()][nxy.getY()] == null)
                _available.add(nxy);
    }

    private Point bestFit(Color color) {
        Point bestFitPoint = null;
        float bestFit = Float.MAX_VALUE;

        for(Point xy : _available) {
            float diff = calcDiff(_bitmap, xy, color);
            if (diff < bestFit) {
                bestFitPoint = xy;
                bestFit = diff;
            }
        }
        return bestFitPoint;
    }

    static float colDiff(Color c1, Color c2) {
        float r = c1.r - c2.r;
        float g = c1.g - c2.g;
        float b = c1.b - c2.b;
        float a = c1.a - c2.a;
        return r * r + g * g + b * b + a * a;
    }

    private ArrayList<Point> getNeighbors(Point xy) {
        ArrayList<Point> ret = new ArrayList<Point>(8);
        for (int dy = -1; dy <= 1; dy++) {
            if (xy.getY() + dy == -1 || xy.getY() + dy == _height)
                continue;
            for (int dx = -1; dx <= 1; dx++) {
                if (xy.getX() + dx == -1 || xy.getX() + dx == _width)
                    continue;
                ret.add(new Point(xy.getX() + dx, xy.getY() + dy));
            }
        }
        return ret;
    }

    private float calcDiff(Color[][] pixels, Point xy, Color c) {
        ArrayList<Float> diffs = new ArrayList<Float>(8);
        for (Point nxy : getNeighbors(xy)) {
            Color nc = pixels[nxy.getX()][nxy.getY()];
            if (nc != null)
                diffs.add(colDiff(nc, c));
        }
        return Collections.min(diffs);
    }

    public Pixmap makePixmap(Color background) {
        Pixmap pixmap = new Pixmap(_width, _height, Pixmap.Format.RGBA8888);
        pixmap.setColor(background);
        pixmap.fill();
        for(int i = 0; i < _width; i++)
            for(int j = 0; j < _height; j++)
                if(_bitmap[i][j] != null)
                    pixmap.drawPixel(i, j, Color.rgba8888(_bitmap[i][j]));
        return pixmap;
    }

    public void dispose() {
        _bitmap = null;
    }
}

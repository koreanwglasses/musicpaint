package com.musicpaint.main;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import org.lwjgl.util.Point;

import java.util.HashSet;
import java.util.Random;

/**
 * Created by fchoi on 2/20/2016.
 */
public class Voronoi {

    ShapeRenderer shapeRenderer;

    public void setBackground(Color background) {
        this.background = background;
    }

    Color background;

    int width;
    int height;

    Random rand;
    Vector2[][] vertices;

    public Voronoi(Color background, int width, int height) {
        shapeRenderer = new ShapeRenderer();

        this.background = background;

        this.width = width;
        this.height = height;

        rand = new Random();
    }

    public void initialize() {
        vertices = new Vector2[width][height];
        for (int i = 0; i < width; i++)
            for (int j = 0; j < height; j++) {
                float x = rand.nextFloat();
                float y = rand.nextFloat();
                vertices[i][j] = new Vector2(i + x - width / 2, -(j + y - height / 2));
            }
    }

    public void render(Color[][] pixels, HashSet<Point> edge) {
        Gdx.gl.glEnable(GL20.GL_DEPTH_TEST);

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        for (int i = 0; i < width; i++)
            for (int j = 0; j < height; j++) {
                Color coneColor;
                if (pixels[i][j] != null) {
                    coneColor = new Color(pixels[i][j]);
                    coneColor.lerp(background, 1 - coneColor.a);
                    coneColor.a = 1;
                    shapeRenderer.setColor(coneColor);
                    shapeRenderer.cone(vertices[i][j].x, vertices[i][j].y, -100, 1.5f, 1.5f, 8);
                }
            }
        shapeRenderer.setColor(background);
        for (Point e : edge) {
            shapeRenderer.cone(vertices[e.getX()][e.getY()].x, vertices[e.getX()][e.getY()].y, -100, 1.5f, 1.5f, 8);
        }
        shapeRenderer.end();
    }

    public void setProjectionMatrix(Matrix4 matrix) {
        shapeRenderer.setProjectionMatrix(matrix);
    }
}

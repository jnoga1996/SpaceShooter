package com.mygdx.game.Objects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.Objects.Abstract.GameObject;

import javax.swing.text.Segment;

public class Enemy extends GameObject {
    private static final float COLLISION_CIRCLE_RADIUS = 45f;
    public static final float WIDTH = 2 * COLLISION_CIRCLE_RADIUS;
    public static final float MAX_SPEED_PER_SECOND = 250f;
    private static final float HEIGHT_OFFSET = 400F;

    public Enemy() {
        y = MathUtils.random(HEIGHT_OFFSET);
        this.collisionCircle = new Circle(x, y, COLLISION_CIRCLE_RADIUS);
    }

    public void setPosition(float x) {
        this.x = x;
        updateCollisionCircle();
    }

    private void updateCollisionCircle() {
        collisionCircle.setX(x);
        collisionCircle.setY(y);
    }

    public void update(float delta) {
        setPosition(x - (MAX_SPEED_PER_SECOND * delta));
    }

    @Override
    public void drawDebug(ShapeRenderer shapeRenderer) {
        shapeRenderer.setColor(Color.RED);
        shapeRenderer.circle(collisionCircle.x, collisionCircle.y,
                collisionCircle.radius, 32);
        shapeRenderer.setColor(Color.WHITE);
    }


}
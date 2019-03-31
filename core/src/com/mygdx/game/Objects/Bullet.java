package com.mygdx.game.Objects;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;

public class Bullet {
    private static final float MAX_SPEED = 600f;
    private static final float COLLISION_RADIUS = 7f;

    private float x;
    private float y;
    private final Circle collisionCircle;

    public Bullet() {
        collisionCircle = new Circle(x, y, COLLISION_RADIUS);
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public void drawDebug(ShapeRenderer shapeRenderer) {
        shapeRenderer.circle(collisionCircle.x, collisionCircle.y,
                collisionCircle.radius);
    }

    public void setPosition(float x, float y) {
        this.x = x;
        this.y = y;
        updateCollisionCircle();
    }

    private void updateCollisionCircle() {
        collisionCircle.setX(x);
        collisionCircle.setY(y);
    }

    public void update(float delta) {
        setPosition(x + MAX_SPEED*delta, y);
    }
}

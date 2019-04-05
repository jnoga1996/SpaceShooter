package com.mygdx.game.Objects;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;

public class Obstacle {

    private static final float MAX_SPEED_PER_SECOND = 100F;
    private static final float COLLISION_CIRCLE_RADIUS = 33f;
    private static final float HEIGHT_OFFSET = 400F;
    public static final float WIDTH = 2 * COLLISION_CIRCLE_RADIUS;

    private final Circle collisionCircle;

    private float x = 0;
    public float getX() {
        return x;
    }
    private float y = 0;

    public Obstacle() {
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

    public void drawDebug(ShapeRenderer shapeRenderer) {
        shapeRenderer.circle(collisionCircle.x, collisionCircle.y,
                collisionCircle.radius);
    }

    public void update(float delta) {
        setPosition(x - (MAX_SPEED_PER_SECOND * delta));
    }

    public boolean isPlayerColliding(Player player) {
        Circle playerCollisionCircle = player.getCollisionCircle();
        return Intersector.overlaps(playerCollisionCircle, collisionCircle);
    }

    public Circle getCollisionCircle() {
        return collisionCircle;
    }
}

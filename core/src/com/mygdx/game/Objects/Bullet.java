package com.mygdx.game.Objects;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.mygdx.game.Objects.Abstract.GameObject;

public class Bullet extends GameObject {
    public static float DEFAULT_SPEED = 450f;
    private float maxSpeed = DEFAULT_SPEED;
    private static final float COLLISION_RADIUS = 7f;

    public Bullet() {
        collisionCircle = new Circle(x, y, COLLISION_RADIUS);
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
        setPosition(x + maxSpeed *delta, y);
    }

    public boolean isObstacleColliding(Obstacle obstacle) {
        Circle obstacleCollisionCircle = obstacle.getCollisionCircle();
        return Intersector.overlaps(obstacleCollisionCircle, collisionCircle);
    }

    public boolean isObjectColliding(GameObject object) {
        Circle obstacleCollisionCircle = object.getCollisionCircle();
        return Intersector.overlaps(obstacleCollisionCircle, collisionCircle);
    }

    public void increaseMaxSpeed(float value) {
        maxSpeed += value;
    }

    public void reset() {
        maxSpeed = DEFAULT_SPEED;
    }
}

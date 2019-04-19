package com.mygdx.game.Objects.Abstract;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;

public abstract class GameObject {

    protected float x, y = 0;
    protected Circle collisionCircle = null;

    public Circle getCollisionCircle() {
        return collisionCircle;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getX() {
        return x;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getY() {
        return y;
    }

    public abstract void drawDebug(ShapeRenderer shapeRenderer);

}

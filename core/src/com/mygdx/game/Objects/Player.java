package com.mygdx.game.Objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.mygdx.game.Objects.Abstract.GameObject;

public class Player extends GameObject {

    private static final float COLLISION_RADIUS = 24f;

    private static final float DIVE_ACCEL = 0.30F;
    private static final float FLY_ACCEL = 5F;
    private static int startingShield = 3;
    private float ySpeed = 0;
    private int hp;
    private int shield;
    private TextureRegion sprite;

    public Player() {
        replenishHp();
        collisionCircle = new Circle(x,y, COLLISION_RADIUS);
        shield = startingShield;
        sprite = new TextureRegion(new Texture(Gdx.files.internal("player.png")));
    }

    @Override
    public void drawDebug(ShapeRenderer shapeRenderer) {
        shapeRenderer.circle(collisionCircle.x, collisionCircle.y,
                collisionCircle.radius);
    }

    public void draw(Batch batch) {
        batch.draw(sprite, x - COLLISION_RADIUS, y - COLLISION_RADIUS,COLLISION_RADIUS, COLLISION_RADIUS,
                2*COLLISION_RADIUS, 2*COLLISION_RADIUS, 1, 1, 270);
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

    public void update() {
        ySpeed -= DIVE_ACCEL;
        setPosition(x, y + ySpeed);
    }

    public void flyUp() {
        ySpeed = FLY_ACCEL;
        setPosition(x, y + ySpeed);
    }

    public void takeDamage() {
        if (getShield() <= 0) {
            hp -= 10;
        } else {
            decreaseShield();
        }
    }

    public void replenishHp() {
        hp = 100;
    }

    public int getHp() {
        return hp;
    }

    public int getShield() { return shield;}

    public void decreaseShield() { shield--;}

    public void increaseShield() {shield++;}

    public void resetShield() { shield = startingShield;}
}

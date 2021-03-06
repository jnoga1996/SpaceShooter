package com.mygdx.game.Objects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.mygdx.game.Objects.Abstract.GameObject;

public class Obstacle extends GameObject {

    private static final float MAX_SPEED_PER_SECOND = 100F;
    private static final float COLLISION_CIRCLE_RADIUS = 33f;
    private static final float HEIGHT_OFFSET = 400F;
    public static final float WIDTH = 2 * COLLISION_CIRCLE_RADIUS;
    private TextureRegion sprite;

    public Obstacle() {
        y = MathUtils.random(HEIGHT_OFFSET);
        this.collisionCircle = new Circle(x, y, COLLISION_CIRCLE_RADIUS);
        sprite = new TextureRegion(new Texture("obstacle.png"));
    }

    public void setPosition(float x) {
        this.x = x;
        updateCollisionCircle();
    }

    private void updateCollisionCircle() {
        collisionCircle.setX(x);
        collisionCircle.setY(y);
    }

    @Override
    public void drawDebug(ShapeRenderer shapeRenderer) {
        shapeRenderer.setColor(Color.GOLD);
        shapeRenderer.circle(collisionCircle.x, collisionCircle.y,
                collisionCircle.radius);
        shapeRenderer.setColor(Color.WHITE);
    }

    public void draw(Batch batch) {
        batch.draw(sprite, x - COLLISION_CIRCLE_RADIUS, y - COLLISION_CIRCLE_RADIUS,
                COLLISION_CIRCLE_RADIUS, COLLISION_CIRCLE_RADIUS, 2*COLLISION_CIRCLE_RADIUS, 2*COLLISION_CIRCLE_RADIUS,
                1, 1, -90);
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

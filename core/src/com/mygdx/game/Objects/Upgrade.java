package com.mygdx.game.Objects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.MathUtils;
import com.mygdx.game.Objects.Abstract.GameObject;

public class Upgrade extends GameObject {
    private UpgradeType type;
    public static final float COLLISION_CIRCLE_RADIUS = 5f;
    private static final float HEIGHT_OFFSET = 400F;
    private static final float MAX_SPEED_PER_SECOND = 100F;

    public Upgrade(UpgradeType type) {
        super();
        this.type = type;
        y = MathUtils.random(HEIGHT_OFFSET);
        this.collisionCircle = new Circle(x, y, COLLISION_CIRCLE_RADIUS);
    }

    public void setPosition(float x) {
        this.x = x;
        updateCollisionCircle();
    }

    public void updateCollisionCircle() {
        collisionCircle.setX(x);
        collisionCircle.setY(y);
    }

    public void update(float delta) {
        setPosition(x - (MAX_SPEED_PER_SECOND * delta));
    }

    @Override
    public void drawDebug(ShapeRenderer shapeRenderer) {
        if (type == UpgradeType.LIFE) {
            shapeRenderer.setColor(Color.RED);
        } else if (type == UpgradeType.MONEY) {
            shapeRenderer.setColor(Color.GOLD);
        } else if (type == UpgradeType.SHIELD) {
            shapeRenderer.setColor(Color.SKY);
        } else if (type == UpgradeType.WEAPON_UPGRADE) {
            shapeRenderer.setColor(Color.PURPLE);
        }

        shapeRenderer.circle(collisionCircle.x, collisionCircle.y,
                collisionCircle.radius);
        shapeRenderer.setColor(Color.WHITE);
    }
}

package com.mygdx.game.Objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.MathUtils;
import com.mygdx.game.Objects.Abstract.GameObject;

public class Upgrade extends GameObject {
    public UpgradeType getType() {
        return type;
    }

    public void setType(UpgradeType type) {
        this.type = type;
    }

    private UpgradeType type;
    public static final float COLLISION_CIRCLE_RADIUS = 15f;
    private static final float HEIGHT_OFFSET = 400F;
    private static final float MAX_SPEED_PER_SECOND = 100F;
    private TextureRegion sprite;

    public Upgrade(UpgradeType type) {
        super();
        this.type = type;
        y = MathUtils.random(HEIGHT_OFFSET);
        this.collisionCircle = new Circle(x, y, COLLISION_CIRCLE_RADIUS);
        if (type == UpgradeType.LIFE) {
            sprite = new TextureRegion(new Texture("healthUpgrade.png"));
        } else if (type == UpgradeType.MONEY) {
            sprite = new TextureRegion(new Texture("moneyUpgrade.png"));
        } else if (type == UpgradeType.SHIELD) {
            sprite = new TextureRegion(new Texture("shieldUpgrade.png"));
        } else if (type == UpgradeType.WEAPON_UPGRADE) {
            sprite = new TextureRegion(new Texture("weaponUpgrade.png"));
        }
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

    public void draw(Batch batch) {
        batch.draw(sprite, x - COLLISION_CIRCLE_RADIUS, y - COLLISION_CIRCLE_RADIUS,
                COLLISION_CIRCLE_RADIUS, COLLISION_CIRCLE_RADIUS,
                2*COLLISION_CIRCLE_RADIUS, 2*COLLISION_CIRCLE_RADIUS, 1, 1, 0);
    }

    public boolean isPlayerColliding(Player player) {
        Circle playerCollisionCircle = player.getCollisionCircle();
        return Intersector.overlaps(playerCollisionCircle, collisionCircle);
    }
}

package com.mygdx.game.Objects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class EnemyBullet extends Bullet {
    private final static float MAX_SPEED = -300f;

    public EnemyBullet() {
        super();
        this.sprite = new TextureRegion(new Texture("enemyBullet.png"));
    }

    public void draw(Batch batch) {
        batch.draw(sprite, x - COLLISION_RADIUS, y - COLLISION_RADIUS, COLLISION_RADIUS, COLLISION_RADIUS,
                2*COLLISION_RADIUS, 2*COLLISION_RADIUS, 1, 1, -90);
    }

    public void update(float delta) {
        setPosition(x + MAX_SPEED*delta, y);
    }
}

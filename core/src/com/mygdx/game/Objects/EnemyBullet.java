package com.mygdx.game.Objects;

import com.mygdx.game.Objects.Abstract.GameObject;

public class EnemyBullet extends Bullet {
    private final static float MAX_SPEED = -450f;

    public EnemyBullet(GameObject owner) {
        super();
    }

    public void update(float delta) {
        setPosition(x + MAX_SPEED*delta, y);
    }
}

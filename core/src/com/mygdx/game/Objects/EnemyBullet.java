package com.mygdx.game.Objects;

public class EnemyBullet extends Bullet {
    private final static float MAX_SPEED = -300f;

    public EnemyBullet() {
        super();
    }

    public void update(float delta) {
        setPosition(x + MAX_SPEED*delta, y);
    }
}

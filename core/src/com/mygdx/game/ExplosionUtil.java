package com.mygdx.game;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.Objects.Explosion;

public class ExplosionUtil {
    private Array<Explosion> explosions;

    public ExplosionUtil(Array<Explosion> explosions) {
        this.explosions = explosions;
    }

    public void addExplosion(Explosion explosion) {
        explosions.add(explosion);
    }

    public void addExplosion(float x, float y) {
        Explosion explosion = new Explosion();
        explosion.setX(x);
        explosion.setY(y);

        explosions.add(explosion);
    }

    public void removeExplosion(Explosion explosion) {
        explosions.removeValue(explosion, true);
    }

    public Array<Explosion> getExplosions() {
        return explosions;
    }
}

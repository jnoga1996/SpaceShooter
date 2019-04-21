package com.mygdx.game.Objects.Utils;

import com.badlogic.gdx.utils.Array;
import com.mygdx.game.Objects.Abstract.GameObject;
import com.mygdx.game.Objects.EnemyBullet;
import com.mygdx.game.Objects.Player;

public class EnemyBulletsUtil{
    private Array<EnemyBullet> bullets;
    private Player player;

    public EnemyBulletsUtil(Array<EnemyBullet> bullets, Player player) {
        this.bullets = bullets;
        this.player = player;
    }

    public void createNewBullet(GameObject object) {
        EnemyBullet bullet = new EnemyBullet();
        bullet.setPosition(object.getX(), object.getY());
        bullets.add(bullet);
    }

    public void checkIfNewBulletIsNeeded(GameObject object) {
        if (bullets.isEmpty()) {
            createNewBullet(object);
        } else {
            EnemyBullet bullet = bullets.peek();
            if (bullet.getX() < 0  && object.getX() > player.getX()) {
                createNewBullet(object);
            }
        }
    }

    public void updateBullets(float delta, GameObject object) {
        for (EnemyBullet bullet : bullets) {
            bullet.update(delta);

        }
        checkIfNewBulletIsNeeded(object);
        removeOffScreenBullets();
    }

    public void removeOffScreenBullets() {
        if (bullets.isEmpty()) {
            EnemyBullet firstBullet = bullets.peek();
            if (firstBullet.getX() < 0)
                bullets.removeValue(firstBullet, true);
        }
    }
}

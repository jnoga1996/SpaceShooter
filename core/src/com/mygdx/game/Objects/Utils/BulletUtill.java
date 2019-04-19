package com.mygdx.game.Objects.Utils;

import com.badlogic.gdx.utils.Array;
import com.mygdx.game.Objects.Abstract.GameObject;
import com.mygdx.game.Objects.Bullet;
import com.mygdx.game.Objects.Obstacle;

public class BulletUtill {
    private Array<Bullet> bullets;
    private GameObject object;
    private float worldWidth;

    public BulletUtill(Array<Bullet> bullets, GameObject object, float worldWidth) {
        this.bullets = bullets;
        this.object = object;
        this.worldWidth = worldWidth;
    }

    public void createNewBullet() {
        Bullet bullet = new Bullet();
        bullet.setPosition(object.getX(), object.getY());
        bullets.add(bullet);
    }

    public void checkIfNewBulletIsNeeded() {
        if (bullets.isEmpty()) {
            createNewBullet();
        } else {
            Bullet bullet = bullets.peek();
            if (bullet.getX() > worldWidth) {
                createNewBullet();
            }
        }
    }

    public void updateBullets(float delta) {
        for (Bullet bullet : bullets) {
            bullet.update(delta);
        }
        checkIfNewBulletIsNeeded();
        removeOffScreenBullets();
    }

    public void removeOffScreenBullets() {
        if (bullets.isEmpty()) {
            Bullet firstBullet = bullets.peek();
            if (firstBullet.getX() > worldWidth)
                bullets.removeValue(firstBullet, true);
        }
    }

    public void checkForBulletCollisionWithObstacle(Array<Obstacle> obstacles, float score) {
        for (Bullet bullet : bullets) {
            for (Obstacle obstacle : obstacles) {
                if (bullet.isObstacleColliding(obstacle)) {
                    obstacles.removeValue(obstacle, true);
                    bullets.removeValue(bullet, true);
                    score += 10;
                }
            }
        }
    }
}

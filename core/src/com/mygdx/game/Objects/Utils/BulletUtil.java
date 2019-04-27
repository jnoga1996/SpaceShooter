package com.mygdx.game.Objects.Utils;

import com.badlogic.gdx.utils.Array;
import com.mygdx.game.Objects.Abstract.GameObject;
import com.mygdx.game.Objects.Bullet;
import com.mygdx.game.Objects.Enemy;
import com.mygdx.game.Objects.Obstacle;
import com.mygdx.game.Objects.Score;

public class BulletUtil {
    protected Array<Bullet> bullets;
    protected float worldWidth;
    private final static int POINTS_FOR_OBSTACLE = 25;
    private final static int POINTS_FOR_ENEMY = 25;

    public BulletUtil(Array<Bullet> bullets, float worldWidth) {
        this.bullets = bullets;
        this.worldWidth = worldWidth;
    }

    public void createNewBullet(GameObject object) {
        Bullet bullet = new Bullet();
        bullet.setPosition(object.getX(), object.getY());
        bullets.add(bullet);
    }

    public void checkIfNewBulletIsNeeded(GameObject object) {
        if (bullets.isEmpty()) {
            createNewBullet(object);
        } else {
            Bullet bullet = bullets.peek();
            if (bullet.getX() > worldWidth) {
                createNewBullet(object);
            }
        }
    }

    public void updateBullets(float delta, GameObject object) {
        for (Bullet bullet : bullets) {
            bullet.update(delta);
        }
        checkIfNewBulletIsNeeded(object);
        removeOffScreenBullets();
    }

    public void removeOffScreenBullets() {
        if (bullets.isEmpty()) {
            Bullet firstBullet = bullets.peek();
            if (firstBullet.getX() > worldWidth)
                bullets.removeValue(firstBullet, true);
        }
    }

    public void checkForBulletCollisionWithObstacle(Array<Obstacle> obstacles, Score score) {
        for (Bullet bullet : bullets) {
            for (Obstacle obstacle : obstacles) {
                if (bullet.isObstacleColliding(obstacle)) {
                    obstacles.removeValue(obstacle, true);
                    bullets.removeValue(bullet, true);
                    score.increaseScore(POINTS_FOR_OBSTACLE);
                }
            }
        }
    }

    public void checkForCollisonWithEnemy(Array<Enemy> enemies, Score score) {
        for (Bullet bullet : bullets) {
            for (Enemy enemy : enemies) {
                if (bullet.isObjectColliding(enemy)) {
                    enemies.removeValue(enemy, true);
                    bullets.removeValue(bullet, true);
                    score.increaseScore(POINTS_FOR_ENEMY);
                }
            }
        }
    }
}

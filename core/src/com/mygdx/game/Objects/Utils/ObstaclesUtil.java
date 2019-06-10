package com.mygdx.game.Objects.Utils;

import com.badlogic.gdx.utils.Array;
import com.mygdx.game.ExplosionUtil;
import com.mygdx.game.Objects.Obstacle;
import com.mygdx.game.Objects.Player;

import java.util.Random;

public class ObstaclesUtil {
    private Array<Obstacle> obstacles;
    private final float WORLD_WIDTH;
    private static final int MAX_NUMBER_OF_OBSTACLES = 3;
    private ExplosionUtil explosionUtil;

    public ObstaclesUtil(Array<Obstacle> obstacles, float worldWidth, ExplosionUtil explosionUtil) {
        this.obstacles = obstacles;
        this.WORLD_WIDTH = worldWidth;
        this.explosionUtil = explosionUtil;
    }

    public void createNewObstacle(int numberOfObstacle) {
        Obstacle obstacle = new Obstacle();
        obstacle.setPosition(WORLD_WIDTH + Obstacle.WIDTH);
        obstacles.add(obstacle);
    }

    public void checkIfNewObstacleIsNeeded() {
        if (obstacles.isEmpty()) {
            createNewObstacle(1);
        } else {
            Obstacle obstacle = obstacles.peek();
            if (obstacle.getX() < WORLD_WIDTH && obstacles.size <= MAX_NUMBER_OF_OBSTACLES) {
                Random rand = new Random();
                createNewObstacle(1);
            }
        }
    }

    public void removeOffScreenObstacles() {
        if (!obstacles.isEmpty()) {
            Obstacle firstObstacle = obstacles.peek();
            if (firstObstacle.getX() < -Obstacle.WIDTH) {
                obstacles.removeValue(firstObstacle, true);
            }
        }
    }

    public void updateObstacles(float delta) {
        for (Obstacle obstacle : obstacles) {
            obstacle.update(delta);
        }
        checkIfNewObstacleIsNeeded();
        removeOffScreenObstacles();
    }

    public boolean checkForCollision(Player player) {
        for (Obstacle obstacle : obstacles) {
            if (obstacle.isPlayerColliding(player)) {
                obstacles.removeValue(obstacle, true);
                explosionUtil.addExplosion(player.getX(), player.getY());
                return true;
            }
        }
        return false;
    }
}

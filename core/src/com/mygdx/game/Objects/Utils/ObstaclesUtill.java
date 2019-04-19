package com.mygdx.game.Objects.Utils;

import com.badlogic.gdx.utils.Array;
import com.mygdx.game.Objects.Obstacle;
import com.mygdx.game.Objects.Player;

public class ObstaclesUtill {
    private Array<Obstacle> obstacles;
    private final float WORLD_WIDTH;

    public ObstaclesUtill(Array<Obstacle> obstacles, float worldWidth) {
        this.obstacles = obstacles;
        this.WORLD_WIDTH = worldWidth;
    }

    public void createNewObstacle() {
        Obstacle obstacle = new Obstacle();
        obstacle.setPosition(WORLD_WIDTH + Obstacle.WIDTH);
        obstacles.add(obstacle);
    }

    public void checkIfNewObstacleIsNeeded() {
        if (obstacles.isEmpty()) {
            createNewObstacle();
        } else {
            Obstacle obstacle = obstacles.peek();
            if (obstacle.getX() < WORLD_WIDTH) {
                createNewObstacle();
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
                return true;
            }
        }
        return false;
    }
}

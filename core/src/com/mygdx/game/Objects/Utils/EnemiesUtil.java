package com.mygdx.game.Objects.Utils;

import com.badlogic.gdx.utils.Array;
import com.mygdx.game.Objects.Enemy;
import com.mygdx.game.Objects.Player;

import java.util.Random;

public class EnemiesUtil {
    private Array<Enemy> enemies;
    private float WORLD_WIDTH;
    private int MAX_NUMBER_OF_ENEMIES;

    public EnemiesUtil(Array<Enemy> enemies, float worldWidth, int maxNumberOfEnemies) {
        this.enemies = enemies;
        this.WORLD_WIDTH = worldWidth;
        this.MAX_NUMBER_OF_ENEMIES = maxNumberOfEnemies;
    }

    public void createNewEnemy(int enemiesToCreate) {
        for (int i = 0; i < enemiesToCreate; i++) {
            Enemy enemy = new Enemy();
            enemy.setPosition(WORLD_WIDTH + Enemy.WIDTH);
            enemies.add(enemy);
        }
    }

    public void checkIfNewEnemyIsNeeded() {
        if (enemies.isEmpty()) {
            createNewEnemy(1);
        } else {
            Enemy enemy = enemies.peek();
            if (enemy.getX() < WORLD_WIDTH && enemies.size < MAX_NUMBER_OF_ENEMIES) {
                Random rand = new Random();
                createNewEnemy(rand.nextInt(MAX_NUMBER_OF_ENEMIES));
            }

        }
    }

    public void updateEnemies(float delta) {
        for (Enemy enemy : enemies) {
            enemy.update(delta);
        }
        checkIfNewEnemyIsNeeded();
        removeOffScreenEnemies();
    }

    public void removeOffScreenEnemies() {
        if (!enemies.isEmpty()) {
            Enemy firstEnemy = enemies.peek();
            if (firstEnemy.getX() < -Enemy.WIDTH) {
                enemies.removeValue(firstEnemy, true);
            }
        }
    }

    public void checkForCollision(Player player) {
        for(Enemy enemy : enemies) {
            if (enemy.isEnemyColliding(player)) {
                enemies.removeValue(enemy, true);
                player.takeDamage();
            }
        }
    }

}

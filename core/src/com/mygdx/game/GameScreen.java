package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.Objects.Bullet;
import com.mygdx.game.Objects.Enemy;
import com.mygdx.game.Objects.Obstacle;
import com.mygdx.game.Objects.Player;
import com.mygdx.game.Objects.Utils.BulletUtill;
import com.mygdx.game.Objects.Utils.ObstaclesUtill;

import java.util.Random;

public class GameScreen extends ScreenAdapter {
    private static final float WORLD_WIDTH = 640;
    private static final float WORLD_HEIGHT = 480;
    private static final float GAP_BETWEEN_OBSTACLES = 600F;
    private int score = 0;
    private static final int MAX_NUMBER_OF_ENEMIES = 3;

    private ShapeRenderer shapeRenderer;
    private Viewport viewport;
    private Camera camera;
    private SpriteBatch batch;
    private BitmapFont bitmapFont;
    private GlyphLayout glyphLayout;

    private Player player = new Player();
    private Array<Obstacle> obstacles = new Array<Obstacle>();
    private Array<Bullet> bullets = new Array<Bullet>();
    private Array<Enemy> enemies = new Array<Enemy>(MAX_NUMBER_OF_ENEMIES);
    private Array<Bullet> enemyBullets = new Array<Bullet>();

    private BulletUtill bulletUtill = new BulletUtill(bullets, player, WORLD_WIDTH);
    private BulletUtill enemyBulletUtill = new BulletUtill(bullets, player, WORLD_WIDTH);
    private ObstaclesUtill obstaclesUtill = new ObstaclesUtill(obstacles, WORLD_WIDTH);

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
    }

    @Override
    public void show() {
        camera = new OrthographicCamera();
        camera.position.set(WORLD_WIDTH / 2, WORLD_HEIGHT / 2, 0);
        camera.update();
        viewport = new FitViewport(WORLD_WIDTH, WORLD_HEIGHT, camera);
        shapeRenderer = new ShapeRenderer();
        batch = new SpriteBatch();
        player.setPosition(WORLD_WIDTH/4, WORLD_HEIGHT/4);
        bitmapFont = new BitmapFont();
        glyphLayout = new GlyphLayout();

    }

    @Override
    public void render(float delta) {
        clearScreen();
        batch.setProjectionMatrix(camera.projection);
        batch.setTransformMatrix(camera.view);
        batch.begin();
        drawScore();
        batch.end();

        shapeRenderer.setProjectionMatrix(camera.projection);
        shapeRenderer.setTransformMatrix(camera.view);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);

        drawDebug();
        shapeRenderer.end();

        update(delta);
    }

    private void update(float delta) {
        player.update();
        if (Gdx.input.isTouched())
            player.flyUp();
        blockPlayerLeavingTheWorld();

        obstaclesUtill.updateObstacles(delta);
        updateEnemies(delta);

        bulletUtill.updateBullets(delta);
        enemyBulletUtill.updateBullets(delta);

        if (obstaclesUtill.checkForCollision(player)) {
            restart();
        }

        checkIfPlayerWasHit();
        bulletUtill.checkForBulletCollisionWithObstacle(obstacles, score);

    }

    private boolean checkIfPlayerWasHit() {
        return false;
    }

    private void updateEnemies(float delta) {
        for (Enemy enemy : enemies) {
            enemy.update(delta);
        }
        checkIfNewEnemyIsNeeded();
        removeOffScreenEnemies();
    }

    private void removeOffScreenEnemies() {
        if (!enemies.isEmpty()) {
            Enemy firstEnemy = enemies.peek();
            if (firstEnemy.getX() < -Enemy.WIDTH) {
                enemies.removeValue(firstEnemy, true);
            }
        }
    }

    private void blockPlayerLeavingTheWorld() {
        player.setPosition(player.getX(),
                MathUtils.clamp(player.getY(), 0, WORLD_HEIGHT));
    }

    private void clearScreen() {
        Gdx.gl.glClearColor(Color.BLACK.r, Color.BLACK.g,
                Color.BLACK.b, Color.BLACK.a);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }


    private void createNewEnemy(int enemiesToCreate) {
        for (int i = 0; i < enemiesToCreate; i++) {
            Enemy enemy = new Enemy();
            enemy.setPosition(WORLD_WIDTH + Enemy.WIDTH);
            enemies.add(enemy);
        }
    }

    private void checkIfNewEnemyIsNeeded() {
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

    private void drawDebug() {
        player.drawDebug(shapeRenderer);
        for (Bullet bullet : bullets) {
            bullet.drawDebug(shapeRenderer);
        }

        for (Obstacle obstacle : obstacles) {
            obstacle.drawDebug(shapeRenderer);
        }

        for (Enemy enemy : enemies) {
            enemy.drawDebug(shapeRenderer);
        }
    }

    private void restart() {
        player.setPosition(WORLD_WIDTH/4, WORLD_HEIGHT/2);
        obstacles.clear();
        score = 0;
    }

    private void drawScore() {
        String scoreString = Integer.toString(score);
        glyphLayout.setText(bitmapFont, scoreString);
        bitmapFont.draw(batch, scoreString, 50, 50);
    }
}

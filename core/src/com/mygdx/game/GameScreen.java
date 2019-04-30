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
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.Objects.Bullet;
import com.mygdx.game.Objects.Enemy;
import com.mygdx.game.Objects.EnemyBullet;
import com.mygdx.game.Objects.Obstacle;
import com.mygdx.game.Objects.Player;
import com.mygdx.game.Objects.Score;
import com.mygdx.game.Objects.Utils.BulletUtil;
import com.mygdx.game.Objects.Utils.EnemiesUtil;
import com.mygdx.game.Objects.Utils.EnemyBulletsUtil;
import com.mygdx.game.Objects.Utils.ObstaclesUtil;

public class GameScreen extends ScreenAdapter {
    private static final float WORLD_WIDTH = 640;
    private static final float WORLD_HEIGHT = 480;
    private static final float GAP_BETWEEN_OBSTACLES = 600F;
    private static final int MAX_NUMBER_OF_ENEMIES = 3;

    private ShapeRenderer shapeRenderer;
    private Viewport viewport;
    private Camera camera;
    private SpriteBatch batch;
    private BitmapFont bitmapFont;
    private GlyphLayout glyphLayout;

    private Player player = new Player();
    private Array<Obstacle> obstacles = new Array<Obstacle>(3);
    private Array<Bullet> bullets = new Array<Bullet>();
    private Array<Enemy> enemies = new Array<Enemy>(MAX_NUMBER_OF_ENEMIES);
    private Array<EnemyBullet> enemyBullets = new Array<EnemyBullet>(MAX_NUMBER_OF_ENEMIES);

    private BulletUtil bulletUtil = new BulletUtil(bullets, WORLD_WIDTH);
    private ObstaclesUtil obstaclesUtill = new ObstaclesUtil(obstacles, WORLD_WIDTH);
    private EnemiesUtil enemiesUtill = new EnemiesUtil(enemies, WORLD_WIDTH, MAX_NUMBER_OF_ENEMIES);
    private EnemyBulletsUtil enemyBulletsUtil = new EnemyBulletsUtil(enemyBullets, player);
    private Score score = new Score();

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
        drawHp();
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
        enemiesUtill.updateEnemies(delta);

        for (Enemy enemy : enemies) {
            enemyBulletsUtil.updateBullets(delta, enemy);
        }

        bulletUtil.updateBullets(delta, player);

        enemiesUtill.checkForCollision(player);

        if (obstaclesUtill.checkForCollision(player)) {
            player.takeDamage();
        }

        enemyBulletsUtil.checkForCollisonWithPlayer(player);

        if (player.getHp() <= 0) {
            restart();
        }

        bulletUtil.checkForBulletCollisionWithObstacle(obstacles, score);
        bulletUtil.checkForCollisonWithEnemy(enemies, score);

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

    private void drawDebug() {
        shapeRenderer.rect(0,0,100, 20);
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

        for (Bullet bullet: enemyBullets) {
            bullet.drawDebug(shapeRenderer);
        }
    }

    private void restart() {
        player.setPosition(WORLD_WIDTH/4, WORLD_HEIGHT/2);
        obstacles.clear();
        enemies.clear();
        bullets.clear();
        enemyBullets.clear();
        player.replenishHp();
        score.reset();
    }

    private void drawScore() {
        String scoreString = Integer.toString(score.getScore());
        glyphLayout.setText(bitmapFont, scoreString);
        bitmapFont.draw(batch, scoreString, 50, 50);
    }

    private void drawHp() {
        String hpString = Integer.toString(player.getHp());
        glyphLayout.setText(bitmapFont, hpString);
        bitmapFont.draw(batch, hpString, 100, 50);
    }
}

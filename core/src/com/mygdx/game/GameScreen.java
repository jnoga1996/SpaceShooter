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
import com.mygdx.game.Objects.Obstacle;
import com.mygdx.game.Objects.Player;

public class GameScreen extends ScreenAdapter {
    private static final float WORLD_WIDTH = 640;
    private static final float WORLD_HEIGHT = 480;
    private static final float GAP_BETWEEN_OBSTACLES = 600F;
    private int score = 0;

    private ShapeRenderer shapeRenderer;
    private Viewport viewport;
    private Camera camera;
    private SpriteBatch batch;
    private BitmapFont bitmapFont;
    private GlyphLayout glyphLayout;

    private Player player = new Player();
    private Array<Obstacle> obstacles = new Array<Obstacle>();
    private Array<Bullet> bullets = new Array<Bullet>();

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

        updateObstacles(delta);
        updateBullets(delta);

        if (checkForCollision()) {
            restart();
        }

        checkForBulletCollisionWithObstacle();

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

    private void createNewObstacle() {
        Obstacle obstacle = new Obstacle();
        obstacle.setPosition(WORLD_WIDTH + Obstacle.WIDTH);
        obstacles.add(obstacle);
    }

    private void createNewBullet() {
        Bullet bullet = new Bullet();
        bullet.setPosition(player.getX(), player.getY());
        bullets.add(bullet);
    }

    private void checkIfNewObstacleIsNeeded() {
        if (obstacles.isEmpty()) {
            createNewObstacle();
        } else {
            Obstacle obstacle = obstacles.peek();
            if (obstacle.getX() < WORLD_WIDTH - GAP_BETWEEN_OBSTACLES) {
                createNewObstacle();
            }
        }
    }

    private void checkIfNewBulletIsNeeded() {
        if (bullets.isEmpty()) {
            createNewBullet();
        } else {
            Bullet bullet = bullets.peek();
            if (bullet.getX() > WORLD_WIDTH) {
                createNewBullet();
            }
        }
    }

    private void removeOffScreenObstacles() {
        if (!obstacles.isEmpty()) {
            Obstacle firstObstacle = obstacles.peek();
            if (firstObstacle.getX() < -Obstacle.WIDTH) {
                obstacles.removeValue(firstObstacle, true);
            }
        }
    }

    private void removeOffScreenBullets() {
        if (bullets.isEmpty()) {
            Bullet firstBullet = bullets.peek();
            if (firstBullet.getX() > WORLD_WIDTH)
                bullets.removeValue(firstBullet, true);
        }
    }

    private void updateObstacles(float delta) {
        for (Obstacle obstacle : obstacles) {
            obstacle.update(delta);
        }
        checkIfNewObstacleIsNeeded();
        removeOffScreenObstacles();
    }

    private void updateBullets(float delta) {
        for (Bullet bullet : bullets) {
            bullet.update(delta);
        }
        checkIfNewBulletIsNeeded();
        removeOffScreenBullets();
    }

    private void drawDebug() {
        player.drawDebug(shapeRenderer);
        for (Bullet bullet : bullets) {
            bullet.drawDebug(shapeRenderer);
        }

        for (Obstacle obstacle : obstacles) {
            obstacle.drawDebug(shapeRenderer);
        }
    }

    private boolean checkForCollision() {
        for (Obstacle obstacle : obstacles) {
            if (obstacle.isPlayerColliding(player)) {
                return true;
            }
        }
        return false;
    }

    private void checkForBulletCollisionWithObstacle() {
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

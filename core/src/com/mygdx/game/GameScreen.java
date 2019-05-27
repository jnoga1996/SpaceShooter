package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.Objects.Bullet;
import com.mygdx.game.Objects.Enemy;
import com.mygdx.game.Objects.EnemyBullet;
import com.mygdx.game.Objects.GameClock;
import com.mygdx.game.Objects.Obstacle;
import com.mygdx.game.Objects.Player;
import com.mygdx.game.Objects.Score;
import com.mygdx.game.Objects.Upgrade;
import com.mygdx.game.Objects.UpgradeType;
import com.mygdx.game.Objects.Utils.BulletUtil;
import com.mygdx.game.Objects.Utils.EnemiesUtil;
import com.mygdx.game.Objects.Utils.EnemyBulletsUtil;
import com.mygdx.game.Objects.Utils.ObstaclesUtil;
import com.mygdx.game.Objects.Utils.UpgradeUtil;

public class GameScreen extends ScreenAdapter {
    private static final float WORLD_WIDTH = 640;
    private static final float WORLD_HEIGHT = 480;
    private static final int MAX_NUMBER_OF_ENEMIES = 3;
    private int menuOffset = 75;
    private int leftOffset = 50;

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
    private Array<Upgrade> upgrades = new Array<Upgrade>();

    private BulletUtil bulletUtil = new BulletUtil(bullets, WORLD_WIDTH);
    private ObstaclesUtil obstaclesUtill = new ObstaclesUtil(obstacles, WORLD_WIDTH);
    private EnemiesUtil enemiesUtill = new EnemiesUtil(enemies, WORLD_WIDTH, MAX_NUMBER_OF_ENEMIES);
    private EnemyBulletsUtil enemyBulletsUtil = new EnemyBulletsUtil(enemyBullets, player);
    private Score score = new Score();

    private UpgradeUtil upgradeUtil = new UpgradeUtil(upgrades, WORLD_WIDTH, bulletUtil);

    private GameClock timer = new GameClock();

    private final Game game;
    public GameScreen(Game game) {
        this.game = game;
    }

    private TextureRegion background;

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

        background = new TextureRegion(new Texture(Gdx.files.internal("background.jpg")),
                (int)WORLD_WIDTH,
                (int)WORLD_HEIGHT
        );
    }

    @Override
    public void render(float delta) {
        clearScreen();
        batch.setProjectionMatrix(camera.projection);
        batch.setTransformMatrix(camera.view);
        batch.begin();
        batch.draw(background, 0, 0);
        drawHp();
        drawScore();
        drawTimeLeft();
        drawShield();
        draw();
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

        upgradeUtil.updateUpgrades(delta);
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

        enemyBulletsUtil.checkForCollisionWithPlayer(player);

        if (player.getHp() <= 0) {
            restart();
        }

        bulletUtil.checkForBulletCollisionWithObstacle(obstacles, score);
        bulletUtil.checkForCollisionWithEnemy(enemies, score);
        upgradeUtil.checkForCollision(player);

        if (timer.levelCleared()) {
            game.setScreen(new StartScreen(game));

            //changeLevel();
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

    private void drawDebug() {
        //shapeRenderer.rect(0,0,100, 20);
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

        for (Upgrade upgrade : upgrades) {
            upgrade.drawDebug(shapeRenderer);
        }
    }

    private void draw() {
        player.draw(batch);
        for (Upgrade upgrade : upgrades) {
            upgrade.draw(batch);
        }
        for (Enemy enemy : enemies) {
            enemy.draw(batch);
        }
        for (Obstacle obstacle : obstacles) {
            obstacle.draw(batch);
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
        timer.reset();
        player.resetShield();
    }

    private void drawScore() {
        String scoreString = "Score: " + Integer.toString(score.getScore());
        glyphLayout.setText(bitmapFont, scoreString);
        bitmapFont.draw(batch, scoreString, leftOffset, 50);
    }

    private void drawHp() {
        String hpString = "Hp: " + Integer.toString(player.getHp());
        glyphLayout.setText(bitmapFont, hpString);
        bitmapFont.draw(batch, hpString, leftOffset + menuOffset, 50);
    }

    private void drawShield() {
        String shieldPointsString = "Shield: " + Integer.toString(player.getShield());
        glyphLayout.setText(bitmapFont, shieldPointsString);
        bitmapFont.draw(batch, shieldPointsString, leftOffset + 2*menuOffset, 50);
    }

    private void drawTimeLeft() {
        String timeLeftString = "Progress: " + Long.toString((int)timer.getElapsedTimePercentage()) + "%";
        glyphLayout.setText(bitmapFont, timeLeftString);
        bitmapFont.draw(batch, timeLeftString, leftOffset + 3*menuOffset, 50);
    }
}

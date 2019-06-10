package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;

public class GameOverScreen extends ScreenAdapter {
    private static final float WORLD_WIDTH = 640;
    private static final float WORLD_HEIGHT = 480;

    private Stage stage;

    private TextureRegion playTexture;
    private TextureRegion playPressTexture;

    private final Game game;
    public GameOverScreen(Game game) {
        this.game = game;
    }

    public void show() {
        game.dispose();
        stage = new Stage(new FitViewport(WORLD_WIDTH, WORLD_HEIGHT));
        Gdx.input.setInputProcessor(stage);

        Label gameOverLabel = new Label("GAME OVER", new Label.LabelStyle(new BitmapFont(), Color.RED));
        gameOverLabel.setPosition(WORLD_WIDTH/2 - gameOverLabel.getWidth()/2, WORLD_HEIGHT/2);
        gameOverLabel.setAlignment(Align.center);
        gameOverLabel.setFontScale(3);
        stage.addActor(gameOverLabel);

        playTexture = new TextureRegion(new Texture(Gdx.files.internal("Start.png")));
        playPressTexture = new TextureRegion(new Texture(Gdx.files.internal("StartPressed.png")));
        ImageButton play = new ImageButton(new TextureRegionDrawable(new TextureRegion(playTexture)), new TextureRegionDrawable(new TextureRegion(playPressTexture)));
        play.setPosition(WORLD_WIDTH/2, WORLD_HEIGHT/8, Align.center);
        stage.addActor(play);

        play.addListener(new ActorGestureListener() {
            @Override
            public void tap(InputEvent event, float x, float y, int count, int button) {
                super.tap(event, x, y, count, button);
                game.setScreen(new GameScreen(game));
                dispose();
            }});
    }

    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    public void render(float delta) {
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}

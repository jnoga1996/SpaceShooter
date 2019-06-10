package com.mygdx.game.Objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.mygdx.game.Objects.Abstract.GameObject;

public class Explosion extends GameObject {
    protected static final float COLLISION_RADIUS = 45f;
    private TextureRegion textureRegion;
    private int state;
    private boolean shouldBeRemoved = false;
    private final static int MAX_SPRITE_NUMBER = 11;

    public Explosion() {
        state = 0;
    }

    public void draw(Batch batch) {
        String fileName = "explosion" + state + ".png";
        textureRegion = new TextureRegion(new Texture(Gdx.files.internal(fileName)), 250, 250);
        batch.draw(textureRegion, x - COLLISION_RADIUS, y - COLLISION_RADIUS,
                COLLISION_RADIUS, COLLISION_RADIUS,
                2*COLLISION_RADIUS, 2*COLLISION_RADIUS, 2, 2, 0);

        if (state > MAX_SPRITE_NUMBER) {
            shouldBeRemoved = true;
        } else {
            state++;
        }
    }

    @Override
    public void drawDebug(ShapeRenderer shapeRenderer) {

    }

    public boolean getShouldBeRemoved() {
        return shouldBeRemoved;
    }
}

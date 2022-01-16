package com.mygdx.game.GameHelpers;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.GameHelpers.Boxes.Box;

/**
 * Child of the Box object. Creates a Texture to display in a certain position to display the tiles on the ground.
 */
public class BackgroundTile extends Box {
    private final Texture texture;

    /**
     * Creates a background tile to display to the screen
     * @param x x position of the bottom left corner of the tile
     * @param y y position of the bottom left corner of the tile
     * @param texture texture to display this BackgroundTile
     */
    public BackgroundTile(float x, float y, Texture texture) {
        super(x, y, 32, 32);
        this.texture = texture;
    }

    /**
     * Displays the BackgroundTile to the screen
     * @param spriteBatch SpriteBatch to draw the tile with
     */
    public void render(SpriteBatch spriteBatch){
        spriteBatch.begin();
        spriteBatch.draw(texture, x, y, width, height);
        spriteBatch.end();
    }
}

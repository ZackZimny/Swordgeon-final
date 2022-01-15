package com.mygdx.game.GameHelpers;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * Displays the amount of health the player has in the top-left corner of the screen
 */
public class HealthDisplay {
    //turns false when the player is hit
    private boolean isActive = true;
    private TextureRegion activeTexture, inActiveTexture;
    private final float x;
    private final float y;

    /**
     * Creates a HealthDisplay with the given x and y position and Texture
     * @param x x position of the bottom left corner of the Display
     * @param y y position of the bottom left corner of the Display
     * @param texture pre-loaded texture to use to draw the heart; includes both active and inactive states
     */
    public HealthDisplay(float x, float y, Texture texture){
        this.x = x;
        this.y = y;
        loadTextures(texture);
    }

    /**
     * Splits the SpriteSheet in half for each state of the HeartDisplay
     * @param spriteSheet SpriteSheet with both states of the heart
     */
    private void loadTextures(Texture spriteSheet){
        TextureRegion[][] textures = TextureRegion.split(spriteSheet, 16, 16);
        activeTexture = textures[0][0];
        inActiveTexture = textures[0][1];
    }

    /**
     * Displays the HeartDisplay to the screen
     * @param spriteBatch SpriteBatch to draw the HeartDisplay with
     */
    public void render(SpriteBatch spriteBatch){
        spriteBatch.begin();
        if(isActive)
            spriteBatch.draw(activeTexture, x, y);
        else
            spriteBatch.draw(inActiveTexture, x, y);
        spriteBatch.end();
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}

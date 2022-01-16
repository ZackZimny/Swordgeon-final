package com.mygdx.game.Entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Goal extends Entity {
    //Texture that shows when all Enemies have not been defeated yet, and the player cannot advance to the win screen
    private final TextureRegion notOpenTexture;
    //Texture that shows when the Player can advance to the win screen
    private final TextureRegion openTexture;
    //Texture that shows when
    private boolean isOpen = false;

    /**
     * Creates a goal with the x and y positions of the bottom left corner of its hurtBox and a pre-loaded texture
     * @param x x position of the bottom-left corner
     * @param y y position of the bottom-left corner
     * @param texture texture of the flag that contains both open and non-open states to be split
     */
    public Goal(float x, float y, Texture texture){
        super(x, y, 32, 32);
        int FRAME_ROWS = 1;
        int FRAME_COLS = 2;
        TextureRegion[][] textures = TextureRegion.split(texture, texture.getHeight() / FRAME_ROWS,
                texture.getWidth() / FRAME_COLS);
        notOpenTexture = textures[0][0];
        openTexture = textures[0][1];
    }

    /**
     * Displays the flag on the screen
     * @param sb Current SpriteBatch that all sprites are being built with in the LevelRunner
     */
    public void render(SpriteBatch sb){
        TextureRegion currTexture = isOpen ? openTexture : notOpenTexture;
        sb.begin();
        //width and height are 32 because they scale the 16x16 sprite up by 2
        sb.draw(currTexture, hurtBox.getX(), hurtBox.getY(), 32, 32);
        sb.end();
    }

    public void setOpen(boolean open) {
        isOpen = open;
    }

    public boolean isOpen() {
        return isOpen;
    }
}

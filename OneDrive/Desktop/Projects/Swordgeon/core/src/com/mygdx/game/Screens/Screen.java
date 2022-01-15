package com.mygdx.game.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * holds the screen data, handles the fonts, and renders the background for navigation
 */
public class Screen {
    private final Texture boundary, tile;
    private final int tileHeight = Gdx.graphics.getHeight() / 16;
    private final int tileWidth = Gdx.graphics.getWidth() / 16;
    protected FontHandler fontHandler = new FontHandler();
    protected float screenWidth = Gdx.graphics.getWidth();
    protected float screenHeight = Gdx.graphics.getHeight();

    public Screen(){
        boundary = new Texture(Gdx.files.internal("Boundary.png"));
        tile = new Texture(Gdx.files.internal("BackgroundTile.png"));
    }

    /**
     * returns if the cell in position (x, y) is on the edge
     * @param x position of the cell
     * @param y position of the cell
     * @param width number of cells horizontally
     * @param height number of cells vertically
     * @return if cell in position (x, y) is on the edge
     */
    private boolean isEdge(int x, int y, int width, int height){
        return x == 0 || x == width || y == 0 || y == height;
    }

    /**
     * Displays the navigation background to the screen
     * @param spriteBatch spriteBatch to laod the textures to the screen
     */
    protected void renderBackground(SpriteBatch spriteBatch){
        spriteBatch.begin();
        for(int y = 0; y < tileHeight; y++){
            for(int x = 0; x < tileWidth; x++){
                if(isEdge(x, y, tileWidth - 1, tileHeight - 1))
                    spriteBatch.draw(boundary, x * 16, y * 16);
                else
                    spriteBatch.draw(tile, x * 16, y * 16);
            }
        }
        spriteBatch.end();
    }

}

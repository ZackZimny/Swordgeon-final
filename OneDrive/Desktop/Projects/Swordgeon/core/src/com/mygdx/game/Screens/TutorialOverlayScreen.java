package com.mygdx.game.Screens;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class TutorialOverlayScreen extends TutorialScreen {
    private Texture overlay;
    public TutorialOverlayScreen(String path){
        overlay = new Texture(path);
    }

    /**
     * Displays the controlsScreen to the screen
     * @param shapeRenderer shapeRenderer to render buttons
     * @param spriteBatch spriteBatch to draw text
     */
    public void render(ShapeRenderer shapeRenderer, SpriteBatch spriteBatch){
        renderBackground(spriteBatch);
        spriteBatch.begin();
        spriteBatch.draw(overlay, 0, 0);
        spriteBatch.end();
        renderButtons(shapeRenderer, spriteBatch);
    }
}

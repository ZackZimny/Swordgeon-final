package com.mygdx.game.Screens;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class TutorialIntroScreen extends TutorialScreen {
    private final TextureRegion playerTexture, shootingEnemyTexture, trackingEnemyTexture;
    private final BitmapFont font;

    /**
     * Creates a tutorial screen with the specified textures
     * @param playerTexture player's facing down texture
     * @param shootingEnemyTexture shooting enemy texture
     * @param trackingEnemyTexture tracking enemy texture
     */
    public TutorialIntroScreen(TextureRegion playerTexture, TextureRegion shootingEnemyTexture,
                               TextureRegion trackingEnemyTexture){
        this.playerTexture = playerTexture;
        this.shootingEnemyTexture = shootingEnemyTexture;
        this.trackingEnemyTexture = trackingEnemyTexture;
        FreeTypeFontGenerator.FreeTypeFontParameter param = fontHandler.getParameter();
        param.size = 12;
        FreeTypeFontGenerator generator = fontHandler.getFontGenerator();
        font = generator.generateFont(param);
        GlyphLayout titleLayout = new GlyphLayout();
        titleLayout.setText(fontHandler.getFont(), "Tutorial");
    }

    /**
     * gets y from certain parameters
     * @param screenHeight height of the screen
     * @param baseY y to begin the calculation from
     * @param padY distance between each index
     * @param index index to calculate y
     * @return caluclated y
     */
    private float getY(float screenHeight, float baseY, float padY, float index){
        return screenHeight - baseY - padY * index;
    }


    /**
     * Displays the tutorial screen to the screen
     * @param shapeRenderer shapeRenderer to display the buttons
     * @param spriteBatch spriteBatch to render the textures
     */
    public void render(ShapeRenderer shapeRenderer, SpriteBatch spriteBatch){
        float textX = 150;
        float baseY = 75;
        float padY = 100;
        int x = 50;
        renderBackground(spriteBatch);
        spriteBatch.begin();
        fontHandler.getFont().draw(spriteBatch, "Tutorial",
                fontHandler.centerX("Tutorial", screenWidth), screenHeight - 20);
        int SCALE = 4;
        spriteBatch.draw(playerTexture, 23, screenHeight - 160,
                32 * SCALE, 32 * SCALE);
        font.draw(spriteBatch,
                "My name is Bleu! I run when the wasd or arrow keys\nare pressed," +
                        "and I swing my sword\nwhen the space bar is pressed",
                textX,
                getY(screenHeight, baseY, padY, 0));
        spriteBatch.draw(shootingEnemyTexture, x,
                screenHeight - 230, 16 * SCALE, 16 * SCALE);
        font.draw(spriteBatch,
                "This is a shooting enemy. It shoots projectiles out of\nits mouth that I can"
                + "block with my sword.\nThis enemy is stationary.",
                textX,
                getY(screenHeight, baseY, padY, 1));
        spriteBatch.draw(trackingEnemyTexture, x,
                screenHeight - 320, 16 * SCALE, 16 * SCALE);
        font.draw(spriteBatch,
                "This is a tracking enemy. This enemy chases me down\nand bites me when I'm too close \n"
                        + "I can keep some distance with my sword",
                textX,
                getY(screenHeight, baseY, padY, 2));
        spriteBatch.end();
        renderButtons(shapeRenderer, spriteBatch);
    }
}

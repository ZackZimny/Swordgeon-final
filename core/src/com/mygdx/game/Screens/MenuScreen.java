package com.mygdx.game.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

/**
 * generates the main menu screen
 */
public class MenuScreen extends Screen {
    private final BitmapFont redFont;
    private final BitmapFont blueFont;
    private final Button tutorialButton;
    private final Button playButton;
    private final Button optionsButton;
    public MenuScreen(){

        ///////////////////////////////////font handling//////////////////////////////////////////////////////
        FreeTypeFontGenerator fontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("FFFFORWA.TTF"));
        FreeTypeFontGenerator.FreeTypeFontParameter redParam = fontHandler.getParameter();
        redParam.color = Color.RED;
        redFont = fontGenerator.generateFont(redParam);
        FreeTypeFontGenerator.FreeTypeFontParameter blueParam = fontHandler.getParameter();
        blueParam.color = Color.BLUE;
        blueFont = fontGenerator.generateFont(blueParam);
        GlyphLayout layout = new GlyphLayout();
        layout.setText(fontHandler.getFont(), "Swordgeon");

        //////////////////////////////////button handling//////////////////////////////////////////////////
        float screenWidth = Gdx.graphics.getWidth();
        float screenHeight = Gdx.graphics.getHeight();
        float buttonWidthPercentage = 0.8f;
        float buttonHeightPercentage = 0.1f;
        float buttonWidth = screenWidth * buttonWidthPercentage;
        float buttonHeight = screenHeight * buttonHeightPercentage;
        float buttonX = Button.getXByScreenPercentage(buttonWidthPercentage, screenWidth);
        playButton = new Button(buttonX, 160f, buttonWidth, buttonHeight,
                "Play", Color.BLUE);
        tutorialButton = new Button(buttonX, 100f, buttonWidth, buttonHeight,
                "Tutorial", Color.RED);
        optionsButton = new Button(buttonX, 40f, buttonWidth, buttonHeight,
                "Options", Color.PURPLE);
    }

    /**
     * displays the title screen
     * @param spriteBatch spriteBatch to render the title
     */
    private void renderTitle(SpriteBatch spriteBatch){
        float screenWidth = Gdx.graphics.getWidth();
        float screenHeight = Gdx.graphics.getHeight();
        float textX = fontHandler.centerX("Swordgeon", screenWidth);
        spriteBatch.begin();
        redFont.draw(spriteBatch,"Swordgeon", textX - 3,
                screenHeight - 15);
        blueFont.draw(spriteBatch, "Swordgeon", textX,
                screenHeight - 17);
        spriteBatch.end();
    }

    /**
     * displays the menuScreen to the screen
     * @param shapeRenderer shapeRenderer to create the Buttons and background
     * @param spriteBatch SpriteBatch to load text
     */
    public void render(ShapeRenderer shapeRenderer, SpriteBatch spriteBatch){
        renderBackground(spriteBatch);
        renderTitle(spriteBatch);
        //spriteBatch.draw(overlay, 0, 0);
        tutorialButton.render(shapeRenderer, spriteBatch);
        playButton.render(shapeRenderer, spriteBatch);
        optionsButton.render(shapeRenderer, spriteBatch);
    }

    public Button getTutorialButton() {
        return tutorialButton;
    }

    public Button getPlayButton() {
        return playButton;
    }

    public Button getOptionsButton() {
        return optionsButton;
    }
}
